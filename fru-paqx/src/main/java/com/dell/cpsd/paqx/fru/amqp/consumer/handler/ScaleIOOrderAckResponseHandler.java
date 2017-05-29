/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.storage.capabilities.api.OrderAckMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * Handles incoming ScaleIO OrderAckMessage messages.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class ScaleIOOrderAckResponseHandler extends DefaultMessageHandler<OrderAckMessage> implements AsyncAcknowledgement<OrderAckMessage>{
    private final AsyncRequestHandler<OrderAckMessage> asyncRequestHandler = new AsyncRequestHandler<>();

    @Autowired
    public ScaleIOOrderAckResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer) {
        super(OrderAckMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
    }

    @Override
    protected void executeOperation(final OrderAckMessage message) throws Exception {
        final String correlationId = message.getMessageProperties().getCorrelationId();
        asyncRequestHandler.complete(correlationId, message);
    }

    @Override
    public CompletableFuture<OrderAckMessage> register(final String correlationId) {
        return asyncRequestHandler.register(correlationId);
    }
}
