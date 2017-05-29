/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
**/


package com.dell.cpsd.paqx.fru.service.integration;

import com.dell.converged.capabilities.compute.discovered.nodes.api.IpMacAddress;
import com.dell.converged.capabilities.compute.discovered.nodes.api.MessageProperties;
import com.dell.converged.capabilities.compute.discovered.nodes.api.NodeEventDataDiscovered;
import com.dell.converged.capabilities.compute.discovered.nodes.api.NodeEventDiscovered;
import com.dell.cpsd.paqx.fru.rest.repository.InMemoryNodeDiscoveredRepository;
import com.dell.cpsd.paqx.fru.amqp.config.NodeDiscoveredAmqpConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 *
 *
 *
 *
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NodeEventDiscoveredIT
{
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    InMemoryNodeDiscoveredRepository repository;

    @Test
    public void nodeEventDiscoveredTest(){
        // Send node event discovered
        NodeEventDiscovered event = new NodeEventDiscovered();
        MessageProperties properties = new MessageProperties();
        properties.setCorrelationId(UUID.randomUUID().toString());
        properties.setReplyTo("test");
        properties.setTimestamp(new Date());

        event.setMessageProperties(properties);

        NodeEventDataDiscovered data = new NodeEventDataDiscovered();
        data.setNodeType("compute");
        data.setNodeId("1234");
        List<IpMacAddress> ipMacs = new ArrayList<IpMacAddress>();
        IpMacAddress ipMac = new IpMacAddress("172.31.128.2", "2c:60:0c:ad:d5:ba");
        ipMacs.add(ipMac);
        data.setIpMacAddresses(ipMacs);

        event.setNodeEventDataDiscovered(data);

        // Send node discovered over rabbitMQ
        Object test  = rabbitTemplate.convertSendAndReceive(NodeDiscoveredAmqpConfig.EXCHANGE_RACKHD_ADAPTER_NODE_EVENT, "", event);

        // Query the repo make sure it is there

        NodeEventDataDiscovered inmemoryData = repository.find(data.getNodeId());

        assertThat(inmemoryData).isNotNull();
        assertThat(inmemoryData).isEqualTo(data);

    }
}
