/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.storage.capabilities.api.ListStorageResponseMessage;
import com.dell.cpsd.storage.capabilities.api.MdmClusterDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class ScaleIORestToScaleIODomainTransformerTest
{
    private RabbitConfig     config;
    private MessageConverter converter;

    @Before
    public void setUp()
    {
        config = new RabbitConfig();
        converter = config.messageConverter();

    }
    @Test
    public void testNullCase()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        assertNull(transformer.transform(null));
    }

    @Test
    public void testEmptyRep()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        assertTrue(transformer.transform(rep) != null);
    }

    @Test
    public void testEmptyMDMCluster()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        rep.setMdmClusterDataRestRep(new MdmClusterDataRestRep());
        transformer.transform(rep);
    }

    @Test
    public void testPrimitiveScaleIOSystemDataTransformation()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        rep.setId("id1");
        rep.setInstallId("installid1");
        rep.setMdmClusterState("mdmClusterState1");
        rep.setMdmMode("mode1");
        rep.setName("name1");
        rep.setSystemVersionName("systemversionname1");
        rep.setVersion("version1");

        ScaleIOData data = transformer.transform(rep);
        assertTrue(data != null);
        assertTrue(data.getId().equals("id1"));
        assertTrue(data.getInstallId().equals("installid1"));
        assertTrue(data.getMdmClusterState().equals("mdmClusterState1"));
        assertTrue(data.getMdmMode().equals("mode1"));
        assertTrue(data.getName().equals("name1"));
        assertTrue(data.getSystemVersionName().equals("systemversionname1"));
        assertTrue(data.getVersion().equals("version1"));
    }

    @Test
    public void testRealObject() throws Exception
    {
        final Message message = jsonMessage("com.dell.cpsd.list.storage.response", "src/test/resources/scaleIODiscoveryResponsePayload.json");
        final ListStorageResponseMessage entity = (ListStorageResponseMessage) converter.fromMessage(message);
        ScaleIOSystemDataRestRep rep=entity.getScaleIOSystemDataRestRep();

        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();

        final ScaleIOData domainObject = transformer.transform(rep);
        assertTrue(domainObject!=null);
    }

    public Message jsonMessage(String typeId, String contentFileName) throws Exception
    {
        System.out.println("working directory: " + System.getProperty("user.dir"));
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.setHeader("__TypeId__", typeId);

        String content = IOUtils.toString(new FileInputStream(new File(contentFileName)));

        return new Message(content.getBytes(), properties);
    }
}