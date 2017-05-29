/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.converged.capabilities.compute.discovered.nodes.api.NodeEventDiscovered;
import com.dell.cpsd.paqx.fru.amqp.config.NodeDiscoveredAmqpConfig;
import com.dell.cpsd.paqx.fru.rest.repository.InMemoryNodeDiscoveredRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeDiscoveredEventHandler {
    @Autowired
    InMemoryNodeDiscoveredRepository repository;

    @RabbitListener(queues = NodeDiscoveredAmqpConfig.NODE_DISCOVERED_EVENT_QUEUE)
    public void processNodeDiscoveredEvent(NodeEventDiscovered message){
        repository.save(message.getNodeEventDataDiscovered());
    }
}
