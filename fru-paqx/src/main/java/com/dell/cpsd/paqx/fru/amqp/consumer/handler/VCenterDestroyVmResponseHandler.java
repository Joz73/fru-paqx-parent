/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.DestroyVmResponse;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * VCenter VM destroy response handler
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.1
 * @since 1.0
 */
public class VCenterDestroyVmResponseHandler extends DefaultMessageHandler<DestroyVMResponseMessage>
        implements AsyncAcknowledgement<DestroyVmResponse>
{
    private static final Logger                                 LOG                 = LoggerFactory
            .getLogger(VCenterDestroyVmResponseHandler.class);
    private final        AsyncRequestHandler<DestroyVmResponse> asyncRequestHandler = new AsyncRequestHandler<>();

    public VCenterDestroyVmResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(DestroyVMResponseMessage.class, new DefaultMessageValidator<>(), "", errorTransformer);
    }

    @Override
    protected void executeOperation(final DestroyVMResponseMessage destroyVMResponseMessage) throws Exception
    {
        LOG.info("Received message {}", destroyVMResponseMessage);
        final String correlationId = destroyVMResponseMessage.getMessageProperties().getCorrelationId();
        final DestroyVmResponse destroyVmResponse = new DestroyVmResponse(destroyVMResponseMessage.getStatus().value());
        asyncRequestHandler.complete(correlationId, destroyVmResponse);
    }

    @Override
    public CompletableFuture<DestroyVmResponse> register(final String correlationId)
    {
        LOG.info("Setting expectation for  {}", correlationId);
        return asyncRequestHandler.register(correlationId);
    }
}

