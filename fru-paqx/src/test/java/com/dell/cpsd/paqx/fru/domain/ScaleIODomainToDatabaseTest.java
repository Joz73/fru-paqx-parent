/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import com.dell.cpsd.paqx.fru.amqp.config.ConsumerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ContextConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistenceConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistencePropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProducerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProductionConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig;
import com.dell.cpsd.paqx.fru.amqp.config.TestConfig;
import com.dell.cpsd.paqx.fru.rest.repository.DataServiceRepository;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import com.dell.cpsd.storage.capabilities.api.ListStorageResponseMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@ActiveProfiles({"UnitTest"})
@RunWith(SpringRunner.class)
@Import({ConsumerConfig.class, ContextConfig.class, PersistenceConfig.class, PersistencePropertiesConfig.class, ProducerConfig.class,
        ProductionConfig.class, PropertiesConfig.class, RabbitConfig.class, TestConfig.class})
@DataJpaTest
public class ScaleIODomainToDatabaseTest
{
    private RabbitConfig     config;
    private     MessageConverter converter;

    @Before
    public void setUp()
    {
        config = new RabbitConfig();
        converter = config.messageConverter();

    }

    private static int idCount = 0;

    @Autowired
    @Qualifier("test")
    DataServiceRepository repository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void testRealObject() throws Exception
    {
        final Message message = jsonMessage("com.dell.cpsd.list.storage.response", "src/test/resources/scaleIODiscoveryResponsePayload.json");
        final ListStorageResponseMessage entity = (ListStorageResponseMessage) converter.fromMessage(message);
        ScaleIOSystemDataRestRep rep=entity.getScaleIOSystemDataRestRep();

        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();

        final ScaleIOData domainObject = transformer.transform(rep);
        Assert.assertTrue(domainObject!=null);

        UUID randomUUID = UUID.randomUUID();
        Long databaseJobUUID = repository.saveScaleIOData(randomUUID, domainObject);

        FruJob job = testEntityManager.find(FruJob.class, databaseJobUUID);
        ScaleIOData scaleIOData = job.getScaleIO();
        assertTrue(scaleIOData!=null);
        compareDataModels(domainObject, scaleIOData);
    }

    public Message jsonMessage(String typeId, String contentFileName) throws Exception
    {
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.setHeader("__TypeId__", typeId);

        String content = IOUtils.toString(new FileInputStream(new File(contentFileName)));

        return new Message(content.getBytes(), properties);
    }

    @Test
    public void testRepositoryPersistAndRead() throws Exception
    {
        ScaleIOData data = createScaleIODataObject();

        UUID randomUUID = UUID.randomUUID();
        Long databaseJobUUID = repository.saveScaleIOData(randomUUID, data);

        FruJob job = testEntityManager.find(FruJob.class, databaseJobUUID);
        ScaleIOData scaleIOData = job.getScaleIO();

        ScaleIOData actualScaleIOData = testEntityManager.find(ScaleIOData.class, scaleIOData.getUuid());

        compareDataModels(data, scaleIOData);
        compareDataModels(data, actualScaleIOData);
    }

    private ScaleIOData createScaleIODataObject()
    {
        ScaleIOData data = new ScaleIOData(newId(), "scaleIODataName", "scaleIODataInstallID", "mdmNode", "systemVersionName",
                "mdmClusterState", "version");

        List<ScaleIOIP> ipList = new ArrayList<>();
        ScaleIOTiebreakerScaleIOIP ip1 = new ScaleIOTiebreakerScaleIOIP(newId(), "1.2.3.4");
        ScaleIOTiebreakerScaleIOIP ip2 = new ScaleIOTiebreakerScaleIOIP(newId(), "5.6.7.8");
        ipList.add(ip1);
        ipList.add(ip2);

        ip1.setScaleIOData(data);
        ip2.setScaleIOData(data);
        data.setTiebreakerScaleIOList(ipList);

        List<ScaleIOIP> ipList2 = new ArrayList<>();
        ScaleIOPrimaryMDMIP ip3 = new ScaleIOPrimaryMDMIP(newId(), "1.2.3.4");
        ScaleIOPrimaryMDMIP ip4 = new ScaleIOPrimaryMDMIP(newId(), "5.6.7.8");
        ipList2.add(ip3);
        ipList2.add(ip4);

        ip3.setScaleIOData(data);
        ip4.setScaleIOData(data);
        data.setPrimaryMDMIPList(ipList2);

        List<ScaleIOIP> ipList3 = new ArrayList<>();
        ScaleIOSecondaryMDMIP ip5 = new ScaleIOSecondaryMDMIP(newId(), "1.2.3.4");
        ScaleIOSecondaryMDMIP ip6 = new ScaleIOSecondaryMDMIP(newId(), "5.6.7.8");
        ipList3.add(ip5);
        ipList3.add(ip6);

        ip5.setScaleIOData(data);
        ip6.setScaleIOData(data);
        data.setSecondaryMDMIPList(ipList3);

        //Create mdm cluster
        ScaleIOMdmCluster cluster = new ScaleIOMdmCluster(newId(), "clusterName", "clusterState", "clusterMode", 11, 12);

        cluster.setScaleIOData(data);
        data.setMdmCluster(cluster);

        //Add the slaveElement
        ScaleIOSlaveElementInfo slave = new ScaleIOSlaveElementInfo(newId(), 1234, "version1", "slave", "slave-role", "slave-status");
        slave.addIP(new ScaleIOSlaveScaleIOIP(newId(), "10.11.12.13"));
        slave.addIP(new ScaleIOSlaveScaleIOIP(newId(), "11.12.13.14"));
        slave.setMdmCluster(cluster);
        cluster.addSlave(slave);

        //Add the tiebreaker element
        ScaleIOTiebreakerElementInfo tiebreaker = new ScaleIOTiebreakerElementInfo(newId(), 1235, "version2", "tiebreaker",
                "tiebreaker-role", "tiebreaker-status");
        tiebreaker.addIP(new ScaleIOTiebreakerScaleIOIP(newId(), "12.13.14.15"));
        tiebreaker.addIP(new ScaleIOTiebreakerScaleIOIP(newId(), "13.14.15.16"));
        tiebreaker.setMdmCluster(cluster);
        cluster.addTiebreaker(tiebreaker);

        //Add the master element
        ScaleIOMasterElementInfo master = new ScaleIOMasterElementInfo(newId(), 1236, "version3", "master", "master-role");
        master.addIP(new ScaleIOMasterScaleIOIP(newId(), "14.15.16.17"));
        master.addIP(new ScaleIOMasterScaleIOIP(newId(), "15.16.17.18"));
        master.setMdmCluster(cluster);
        cluster.addMaster(master);

        //Add sdc elements.
        for (int i = 0; i < 4; i++)
        {
            ScaleIOSDC sdc = new ScaleIOSDC(newId(), "sdcName" + i, "192.168.1." + i, "guid" + i, "ok");
            sdc.setScaleIOData(data);
            data.addSdc(sdc);
        }

        ScaleIOFaultSet faultSet = new ScaleIOFaultSet(newId(), "faultSetName");
        ScaleIOProtectionDomain protectionDomain = new ScaleIOProtectionDomain(newId(), "protectionDomainName", "stateActive");
        faultSet.setProtectionDomain(protectionDomain);
        protectionDomain.addFaultSet(faultSet);

        protectionDomain.setScaleIOData(data);
        data.addProtectionDomain(protectionDomain);

        ScaleIOStoragePool storagePool = new ScaleIOStoragePool(newId(), "storagePoolName", 10, 20, 30);
        storagePool.setProtectionDomain(protectionDomain);
        protectionDomain.addStoragePool(storagePool);

        ScaleIOStoragePool storagePool2 = new ScaleIOStoragePool(newId(), "storagePoolName2", 40, 50, 60);
        storagePool2.setProtectionDomain(protectionDomain);
        protectionDomain.addStoragePool(storagePool2);

        //Add sds elements.
        for (int i = 0; i < 4; i++)
        {
            ScaleIOSDS sds = new ScaleIOSDS(newId(), "sdsName" + i, "active" + i, 1230 + i);
            sds.setScaleIOData(data);
            data.addSds(sds);

            if (i % 2 == 0)
            {
                sds.setFaultSet(faultSet);
                faultSet.addSDS(sds);
            }
            sds.setProtectionDomain(protectionDomain);
            protectionDomain.addSDS(sds);

            //Add two ips
            ScaleIORoleIP roleIP1 = new ScaleIORoleIP("role1", "192.168.2." + i);
            roleIP1.setSds(sds);
            sds.addRoleIP(roleIP1);

            ScaleIORoleIP roleIP2 = new ScaleIORoleIP("role1", "192.168.3." + i);
            roleIP2.setSds(sds);
            sds.addRoleIP(roleIP2);

            //Add two devices
            ScaleIODevice device1 = new ScaleIODevice(newId(), "deviceName1" + i, "deviceCurrentPathName1" + i);
            device1.setStoragePool(storagePool);
            storagePool.addDevice(device1);

            device1.setSds(sds);
            sds.addDevice(device1);

            ScaleIODevice device2 = new ScaleIODevice(newId(), "deviceName2" + i, "deviceCurrentPathName2" + i);
            device2.setStoragePool(storagePool2);
            storagePool2.addDevice(device2);

            device2.setSds(sds);
            sds.addDevice(device2);
        }
        return data;
    }

    private void compareDataModels(final ScaleIOData data, final ScaleIOData data3)
    {
        assertTrue(data.equals(data3));
    }

    private static String newId()
    {
        return new String("id" + (idCount++));
    }
}
