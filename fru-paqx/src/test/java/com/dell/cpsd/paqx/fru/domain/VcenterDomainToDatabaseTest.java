/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.paqx.fru.domain;

import com.dell.cpsd.paqx.fru.amqp.config.*;
import com.dell.cpsd.paqx.fru.amqp.config.PersistenceConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistencePropertiesConfig;
import com.dell.cpsd.paqx.fru.rest.repository.DataServiceRepository;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterDomainTransformer;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import com.dell.cpsd.storage.capabilities.api.ListStorageResponseMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ActiveProfiles({"UnitTest"})
@RunWith(SpringRunner.class)
@Import({ConsumerConfig.class, ContextConfig.class, PersistenceConfig.class, PersistencePropertiesConfig.class, ProducerConfig.class,
        ProductionConfig.class, PropertiesConfig.class, RabbitConfig.class, TestConfig.class})
@DataJpaTest
public class VcenterDomainToDatabaseTest
{
    private RabbitConfig     config;
    private MessageConverter converter;

    @Autowired
    @Qualifier("test")
    DataServiceRepository repository;

    private static int idCount=0;
    private static int clusterCount=2;
    private static int hostCount=5;

    @Autowired
    TestEntityManager testEntityManager;

    @Before
    public void setUp()
    {
        config = new RabbitConfig();
        converter = config.messageConverter();

    }

    @Test
    public void createDataObject() throws Exception
    {
        // Create a vcenter instance
        VCenter vCenter = new VCenter(newId(), "vc-1");

        // Create datacenter and persist entry
        Datacenter dc = new Datacenter(newId(), "dc-1");
        dc.setvCenter(vCenter);
        vCenter.setDatacenterList(Arrays.asList(dc));
        testEntityManager.persist(vCenter);

        Datacenter dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dc.equals(dcQuery));
        assertTrue(vCenter.equals(dcQuery.getvCenter()));

        // create clusters
        List<Cluster> clusterList = new ArrayList<>();
        for(int i=0; i<clusterCount; i++)
        {
            // Create clusters
            Cluster cluster = new Cluster(newId(), newNamedId("domain"));
            cluster.setDatacenter(dc);
            for(int j=0; j<hostCount; j++)
            {
                Host host = new Host(newId(),newNamedId("host"),"on");
                host.setCluster(cluster);
                cluster.addHost(host);
            }
            clusterList.add(cluster);
        }
        dc.setClusterList(clusterList);

        testEntityManager.persist(dc);

        // Validate cluster list for dc is as expected
        dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dcQuery.getClusterList().containsAll(clusterList));

        // Validate cluster exists
        Cluster clusterQuery = testEntityManager.find(Cluster.class, 1l);
        assertTrue(clusterQuery.getDatacenter().equals(dc));
        assertTrue(clusterQuery.equals(clusterList.get(0)));

        clusterQuery = testEntityManager.find(Cluster.class, 2l);
        assertTrue(clusterQuery.getDatacenter().equals(dc));
        assertTrue(clusterQuery.equals(clusterList.get(1)));

        // Add datastores to datacenter
        Datastore ds1 = new Datastore(newId(), "ds-01","VMFS", "/im/a/fake/url1");
        Datastore ds2 = new Datastore(newId(), "ds-02","VMFS", "/im/a/fake/url2");
        Datastore ds3 = new Datastore(newId(), "ds-03","VMFS", "/im/a/fake/url3");
        ds1.setDatacenter(dc);
        ds2.setDatacenter(dc);
        ds3.setDatacenter(dc);

        // Validate no datastores exist within dc
        dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dcQuery.getDatastoreList().isEmpty());

        // Update dc with datastores
        List<Datastore> datastoreList = Arrays.asList(ds1, ds2, ds3);
        dc.setDatastoreList(datastoreList);

        testEntityManager.persist(dc);

        Datastore datastoreQuery = testEntityManager.find(Datastore.class, 1l);
        assertTrue(datastoreQuery.equals(ds1));
        assertTrue(datastoreQuery.getDatacenter().equals(dc));

        datastoreQuery = testEntityManager.find(Datastore.class, 2l);
        assertTrue(datastoreQuery.equals(ds2));
        assertTrue(datastoreQuery.getDatacenter().equals(dc));

        datastoreQuery = testEntityManager.find(Datastore.class, 3l);
        assertTrue(datastoreQuery.equals(ds3));
        assertTrue(datastoreQuery.getDatacenter().equals(dc));

        // Create some dvSwitches
        DVSwitch dvSwitch1 = new DVSwitch(newId(), "dvs-1", true);
        dvSwitch1.setDatacenter(dc);

        DVSwitch dvSwitch2 = new DVSwitch(newId(), "dvs-2", true);
        dvSwitch2.setDatacenter(dc);

        // Do bi-directional mapping & persist
        List<DVSwitch> dvSwitches = Arrays.asList(dvSwitch1, dvSwitch2);
        dc.setDvSwitchList(dvSwitches);

        testEntityManager.persist(dc);

        // Validate dvswitches are mapped to dc
        DVSwitch dvSwitchQuery = testEntityManager.find(DVSwitch.class, 1l);
        assertTrue(dvSwitchQuery.equals(dvSwitch1));
        assertTrue(dvSwitchQuery.getDatacenter().equals(dc));

        // Validate dc mapped to dvswitches
        dcQuery = testEntityManager.find(Datacenter.class, 1l);
        assertTrue(dcQuery.getDvSwitchList().containsAll(dvSwitches));
    }

    @Test
    public void vCenterDiscoveryTest() throws Exception
    {
        final Message message = jsonMessage("com.dell.cpsd.vcenter.discover.response", "src/test/resources/vcenterResponseDiscoveryPayload.json");
        final DiscoveryResponseInfoMessage entity = (DiscoveryResponseInfoMessage) converter.fromMessage(message);

        assertNotNull(entity);
        assertNotNull(entity.getMessageProperties());
        assertEquals("c72445b6-5681-4669-b12f-22eb577b0217", entity.getMessageProperties().getCorrelationId());
        DiscoveryInfoToVCenterDomainTransformer transformer = new DiscoveryInfoToVCenterDomainTransformer();

        final VCenter domainObject = transformer.transform(entity);
        Assert.assertTrue(domainObject!=null);

        UUID randomUUID = UUID.randomUUID();
        Long databaseJobUUID = repository.saveVCenterData(randomUUID, domainObject);

        FruJob job = testEntityManager.find(FruJob.class, databaseJobUUID);
        VCenter vcenterData = job.getVcenter();
        assertTrue(vcenterData!=null);
        compareDataModels(domainObject, vcenterData);
    }

    @Test
    public void testPersistVCenterAndScaleIOObjects() throws Exception
    {
        final Message messageV = jsonMessage("com.dell.cpsd.vcenter.discover.response",
                "src/test/resources/vcenterResponseDiscoveryPayload.json");
        final DiscoveryResponseInfoMessage entityV = (DiscoveryResponseInfoMessage) converter.fromMessage(messageV);

        assertNotNull(entityV);
        assertNotNull(entityV.getMessageProperties());
        assertEquals("c72445b6-5681-4669-b12f-22eb577b0217", entityV.getMessageProperties().getCorrelationId());
        DiscoveryInfoToVCenterDomainTransformer transformerV = new DiscoveryInfoToVCenterDomainTransformer();

        final VCenter domainObjectV = transformerV.transform(entityV);
        Assert.assertTrue(domainObjectV != null);

        UUID randomUUIDV = UUID.randomUUID();
        Long databaseJobUUIDV = repository.saveVCenterData(randomUUIDV, domainObjectV);

        FruJob jobV = testEntityManager.find(FruJob.class, databaseJobUUIDV);
        VCenter vcenterData = jobV.getVcenter();
        assertTrue(vcenterData != null);
        compareDataModels(domainObjectV, vcenterData);

        //Save scaleIO
        final Message message = jsonMessage("com.dell.cpsd.list.storage.response", "src/test/resources/scaleIODiscoveryResponsePayload.json");
        final ListStorageResponseMessage entity = (ListStorageResponseMessage) converter.fromMessage(message);
        ScaleIOSystemDataRestRep rep = entity.getScaleIOSystemDataRestRep();

        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();

        final ScaleIOData domainObject = transformer.transform(rep);
        Assert.assertTrue(domainObject != null);

        Long databaseJobUUID = repository.saveScaleIOData(randomUUIDV, domainObject);

        FruJob job = testEntityManager.find(FruJob.class, databaseJobUUID);
        ScaleIOData scaleIOData = job.getScaleIO();
        assertTrue(scaleIOData != null);
    }

    private Message jsonMessage(String typeId, String contentFileName) throws Exception
    {
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.setHeader("__TypeId__", typeId);

        String content = IOUtils.toString(new FileInputStream(new File(contentFileName)));

        return new Message(content.getBytes(), properties);
    }

    private void compareDataModels(final VCenter data, final VCenter data2)
    {
        assertTrue(data.equals(data2));
    }

    private static String newId()
    {
        return new String("id"+(idCount++));
    }

    private static String newNamedId(String baseName)
    {
        return new String(baseName+"-"+(idCount++));
    }
}