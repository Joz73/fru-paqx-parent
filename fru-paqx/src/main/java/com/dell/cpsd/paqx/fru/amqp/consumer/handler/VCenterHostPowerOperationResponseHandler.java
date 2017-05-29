/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.VCenterHostPowerOperationStatus;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * VCenter Host Power Operation Response Handler
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.1
 * @since 1.0
 */
public class VCenterHostPowerOperationResponseHandler extends DefaultMessageHandler<HostPowerOperationResponseMessage>
        implements AsyncAcknowledgement<VCenterHostPowerOperationStatus>
{
    private static final Logger                                               LOG                 = LoggerFactory
            .getLogger(VCenterHostPowerOperationResponseHandler.class);
    private final        AsyncRequestHandler<VCenterHostPowerOperationStatus> asyncRequestHandler = new AsyncRequestHandler<>();

    public VCenterHostPowerOperationResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(HostPowerOperationResponseMessage.class, new DefaultMessageValidator<>(), "", errorTransformer);
    }

    @Override
    protected void executeOperation(HostPowerOperationResponseMessage hostPowerOperationResponseMessage) throws Exception
    {
        LOG.info("Received message {}", hostPowerOperationResponseMessage);
        final String correlationId = hostPowerOperationResponseMessage.getMessageProperties().getCorrelationId();
        final VCenterHostPowerOperationStatus vCenterHostPowerOperationStatus = new VCenterHostPowerOperationStatus(
                hostPowerOperationResponseMessage.getStatus().value());
        asyncRequestHandler.complete(correlationId, vCenterHostPowerOperationStatus);
    }

    @Override
    public CompletableFuture<VCenterHostPowerOperationStatus> register(String correlationId)
    {
        LOG.info("Setting expectation for  {}", correlationId);
        return asyncRequestHandler.register(correlationId);
    }
}
