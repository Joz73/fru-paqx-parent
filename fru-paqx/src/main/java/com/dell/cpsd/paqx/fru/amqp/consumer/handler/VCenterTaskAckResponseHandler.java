/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.virtualization.capabilities.api.TaskAckMessage;

import java.util.concurrent.CompletableFuture;

/**
 * VCenter Operations Task Response Handler
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.1
 */
public class VCenterTaskAckResponseHandler extends DefaultMessageHandler<TaskAckMessage> implements AsyncAcknowledgement<TaskAckMessage>
{
    private AsyncRequestHandler<TaskAckMessage> asyncRequestHandler;

    public VCenterTaskAckResponseHandler(final ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(TaskAckMessage.class, new DefaultMessageValidator<>(), "", errorTransformer);
        asyncRequestHandler = new AsyncRequestHandler<>();
    }

    @Override
    protected void executeOperation(final TaskAckMessage taskAckMessage) throws Exception
    {
        final String correlationId = taskAckMessage.getMessageProperties().getCorrelationId();
        asyncRequestHandler.complete(correlationId, taskAckMessage);
    }

    @Override
    public CompletableFuture<TaskAckMessage> register(final String correlationId)
    {
        return asyncRequestHandler.register(correlationId);
    }
}

