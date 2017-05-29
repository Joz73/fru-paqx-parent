/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterDomainTransformer;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class VCenterDiscoverResponseHandler extends DefaultMessageHandler<DiscoveryResponseInfoMessage>
        implements AsyncAcknowledgement<VCenter> {
    private static final Logger LOG = LoggerFactory.getLogger(VCenterDiscoverResponseHandler.class);
    private final DiscoveryInfoToVCenterDomainTransformer discoveryInfoToVCenterDomainTransformer;
    private Map<String, CompletableFuture<VCenter>> asyncRequests = new HashMap<>();

    @Autowired
    public VCenterDiscoverResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer,
                                          DiscoveryInfoToVCenterDomainTransformer discoveryInfoToVCenterDomainTransformer) {
        super(DiscoveryResponseInfoMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
        this.discoveryInfoToVCenterDomainTransformer = discoveryInfoToVCenterDomainTransformer;
    }

    @Override
    protected void executeOperation(final DiscoveryResponseInfoMessage discoveryResponseInfoMessage) throws Exception {
        LOG.info("Received message {}", discoveryResponseInfoMessage);
        final String correlationId = discoveryResponseInfoMessage.getMessageProperties().getCorrelationId();

        final VCenter vCenter = discoveryInfoToVCenterDomainTransformer
                .transform(discoveryResponseInfoMessage);

        final CompletableFuture<VCenter> completableFuture = asyncRequests.get(correlationId);

        LOG.info("Completing expectation for  {} {}", correlationId, completableFuture);

        if (completableFuture != null) {
            final boolean complete = completableFuture.complete(vCenter);
            LOG.info("Completed expectation for  {} {} {}", correlationId, completableFuture, complete);
            asyncRequests.remove(correlationId);
        }
    }

    @Override
    public CompletableFuture<VCenter> register(final String correlationId) {
        LOG.info("Setting expectation for  {}", correlationId);
        CompletableFuture<VCenter> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((systemRest, throwable) -> asyncRequests.remove(correlationId));
        asyncRequests.put(correlationId, completableFuture);
        return completableFuture;
    }
}
