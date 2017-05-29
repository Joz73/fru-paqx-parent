/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.amqp.config.ConsumerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ContextConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistenceConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistencePropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProducerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProductionConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig;
import com.dell.cpsd.paqx.fru.amqp.config.TestConfig;
import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 *
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataServiceImplTest
{
    @Autowired
    DataService dataService;

    @Test
    public void getData()
    {
        UUID jobId = UUID.randomUUID();
        dataService.saveScaleioData(jobId, new ScaleIOSystemDataRestRep());
        FRUSystemData taskData = dataService.getData(jobId);
        assertNotNull(taskData);
    }
}
