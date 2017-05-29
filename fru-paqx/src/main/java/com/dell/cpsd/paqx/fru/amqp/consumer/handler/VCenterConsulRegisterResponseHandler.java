/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.virtualization.capabilities.api.ConsulRegisterResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.ResponseInfo;
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
public class VCenterConsulRegisterResponseHandler extends DefaultMessageHandler<ConsulRegisterResponseMessage>
        implements AsyncAcknowledgement<ConsulRegistryResult> {
    private static final Logger LOG = LoggerFactory
            .getLogger(VCenterConsulRegisterResponseHandler.class);
    private Map<String, CompletableFuture<ConsulRegistryResult>> asyncRequests = new HashMap<>();

    @Autowired
    public VCenterConsulRegisterResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer) {
        super(ConsulRegisterResponseMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
    }

    @Override
    protected void executeOperation(final ConsulRegisterResponseMessage responseMessage) throws Exception {
        LOG.info("Received message {}", responseMessage);
        final String correlationId = responseMessage.getMessageProperties().getCorrelationId();

        final ResponseInfo responseInfo = responseMessage.getResponseInfo();
        final boolean success = responseInfo.getMessage().startsWith("SUCCESS");
        final ConsulRegistryResult consulRegistryResult = new ConsulRegistryResult(success, responseInfo.getMessage());
        final CompletableFuture<ConsulRegistryResult> completableFuture = asyncRequests.get(correlationId);
        LOG.info("Completing expectation for  {} {}", correlationId, completableFuture);

        if (completableFuture != null) {
            final boolean complete = completableFuture.complete(consulRegistryResult);
            LOG.info("Completed expectation for  {} {} {}", correlationId, completableFuture, complete);
            asyncRequests.remove(correlationId);
        }
    }

    @Override
    public CompletableFuture<ConsulRegistryResult> register(final String correlationId) {
        LOG.info("Setting expectation for  {}", correlationId);
        CompletableFuture<ConsulRegistryResult> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((systemRest, throwable) -> asyncRequests.remove(correlationId));
        asyncRequests.put(correlationId, completableFuture);
        return completableFuture;
    }
}
