/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.paqx.fru.amqp.model.ListNodesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class RequestResponseMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseMatcher.class);

    private final Map<String, CompletableFuture<List<ListNodesResponse.Node>>> map = new ConcurrentHashMap<>();

    public void onNext(final String correlationId, final CompletableFuture<List<ListNodesResponse.Node>> promise) {
        LOG.debug("Setting expectation for {}", correlationId);
        map.put(correlationId, promise);
    }

    public void received(final String correlationId, ListNodesResponse listNodesResponse) {
        LOG.debug("Received {} details {}", correlationId, listNodesResponse.getNodes());
        final CompletableFuture<List<ListNodesResponse.Node>> promise = map.get(correlationId);
        if (promise != null) {
            LOG.debug("Completing promise for {}", correlationId);
            promise.complete(listNodesResponse.getNodes());
            LOG.debug("Promise completed for {}", correlationId);

            map.remove(correlationId);
        }
    }
}