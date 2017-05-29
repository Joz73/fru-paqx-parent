/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.storage.capabilities.api.OrderInfo;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class ScaleIORemoveResponseHandler extends DefaultMessageHandler<SIONodeRemoveResponseMessage>
        implements AsyncAcknowledgement<OrderInfo>
{
    private static final Logger                                    LOG                 = LoggerFactory
            .getLogger(ScaleIORemoveResponseHandler.class);
    private final        AsyncRequestHandler<OrderInfo>      asyncRequestHandler = new AsyncRequestHandler<>();

    @Autowired
    public ScaleIORemoveResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(SIONodeRemoveResponseMessage.class, new DefaultMessageValidator<>(), EXCHANGE_FRU_RESPONSE, errorTransformer);
    }

    @Override
    protected void executeOperation(final SIONodeRemoveResponseMessage sioNodeRemoveResponseMessage) throws Exception
    {
        LOG.info("Received message {}", sioNodeRemoveResponseMessage);
        final String correlationId = sioNodeRemoveResponseMessage.getMessageProperties().getCorrelationId();
        final OrderInfo orderInfo = sioNodeRemoveResponseMessage.getOrderInfo();
        asyncRequestHandler.complete(correlationId, orderInfo);
    }

    @Override
    public CompletableFuture<OrderInfo> register(final String correlationId)
    {
        return asyncRequestHandler.register(correlationId);
    }
}
