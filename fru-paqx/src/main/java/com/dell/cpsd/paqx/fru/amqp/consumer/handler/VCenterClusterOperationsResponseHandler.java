/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.amqp.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig.EXCHANGE_FRU_RESPONSE;

/**
 * VCenter Cluster Operations Response Handler
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.1
 * @since 1.0
 */
public class VCenterClusterOperationsResponseHandler extends DefaultMessageHandler<ClusterOperationResponseMessage>
        implements AsyncAcknowledgement<ClusterOperationResponse>
{
    private static final Logger                                        LOG                 = LoggerFactory
            .getLogger(VCenterClusterOperationsResponseHandler.class);
    private final        AsyncRequestHandler<ClusterOperationResponse> asyncRequestHandler = new AsyncRequestHandler<>();

    public VCenterClusterOperationsResponseHandler(ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        super(ClusterOperationResponseMessage.class, new DefaultMessageValidator<>(), "", errorTransformer);
    }

    @Override
    protected void executeOperation(final ClusterOperationResponseMessage clusterOperationResponseMessage) throws Exception
    {
        LOG.info("Received message {}", clusterOperationResponseMessage);
        final String correlationId = clusterOperationResponseMessage.getMessageProperties().getCorrelationId();

        final ClusterOperationResponse clusterOperationResponse = new ClusterOperationResponse(
                clusterOperationResponseMessage.getStatus().value());

        asyncRequestHandler.complete(correlationId, clusterOperationResponse);
    }

    @Override
    public CompletableFuture<ClusterOperationResponse> register(final String correlationId)
    {
        LOG.info("Setting expectation for  {}", correlationId);
        return asyncRequestHandler.register(correlationId);
    }
}
