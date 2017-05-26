/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.HostMaintenanceModeResponse;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * VCenter Host Maintenance Completion Response handler.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.1
 * @since 1.0
 */
public class vCenterHostMaintenanceModeResponseHandler extends DefaultMessageHandler<HostMaintenanceModeResponseMessage>
        implements AsyncAcknowledgement<HostMaintenanceModeResponse>
{
    private static final Logger                                           LOG                 = LoggerFactory
            .getLogger(VCenterDestroyVmResponseHandler.class);
    private final        AsyncRequestHandler<HostMaintenanceModeResponse> asyncRequestHandler = new AsyncRequestHandler<>();

    public vCenterHostMaintenanceModeResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(HostMaintenanceModeResponseMessage.class, new DefaultMessageValidator<>(), "", errorTransformer);
    }

    @Override
    protected void executeOperation(final HostMaintenanceModeResponseMessage responseMessage) throws Exception
    {
        LOG.info("Received message {}", responseMessage);
        final String correlationId = responseMessage.getMessageProperties().getCorrelationId();
        final HostMaintenanceModeResponse hostMaintenanceModeResponse = new HostMaintenanceModeResponse(
                responseMessage.getStatus().value());
        asyncRequestHandler.complete(correlationId, hostMaintenanceModeResponse);
    }

    @Override
    public CompletableFuture<HostMaintenanceModeResponse> register(final String correlationId)
    {
        return asyncRequestHandler.register(correlationId);
    }
}