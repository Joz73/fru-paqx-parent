/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.common.rabbitmq.consumer.error.DefaultErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageListener;
import com.dell.cpsd.common.rabbitmq.context.builder.DefaultContainerErrorHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.retrypolicy.DefaultMessageRecoverer;
import com.dell.cpsd.common.rabbitmq.retrypolicy.DefaultRetryPolicy;
import com.dell.cpsd.common.rabbitmq.retrypolicy.DefaultRetryPolicyAdvice;
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.*;
import com.dell.cpsd.paqx.fru.amqp.model.Error;
import com.dell.cpsd.paqx.fru.amqp.model.FruErrorMessage;
import com.dell.cpsd.paqx.fru.amqp.model.MessageProperties;
import com.dell.cpsd.paqx.fru.transformers.DestroyVMDtoToDestroyVMRequestMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterDomainTransformer;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterSystemPropertiesTransformer;
import com.dell.cpsd.paqx.fru.transformers.HostListToHostRepresentationTransformer;
import com.dell.cpsd.paqx.fru.transformers.SDSListDtoToRemoveScaleIOMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;

import javax.annotation.Resource;

/**
 * Configures AMQP message consumers.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Configuration
public class ConsumerConfig {
    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory rabbitConnectionFactory;

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private String replyTo;

    @Autowired
    private Queue responseQueue;

    @Bean
    SimpleMessageListenerContainer fruRequestListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setQueues(responseQueue);
        container.setAdviceChain(new Advice[]{retryPolicyAdvice()});
        container.setMessageListener(fruMessageListener());
        container.setErrorHandler(new DefaultContainerErrorHandler("fruRequestListenerContainer"));

        return container;
    }

    @Bean
    DefaultMessageListener fruMessageListener() {
        return new DefaultMessageListener(messageConverter, listNodesResponseHandler(), listStorageResponseHandler(),
                vCenterDiscoverResponseHandler(), vCenterConsulRegisterResponseHandler(), scaleIOOrderAckResponseHandler(),
                scaleIORemoveResponseHandler(), vCenterDestroyVmResponseHandler(), vCenterHostPowerOperationResponseHandler(),
                vCenterHostMaintenanceModeResponseHandler(), vCenterClusterOperationsResponseHandler(), coprHDConsulRegisterResponseHandler());
    }

    @Bean
    ListNodesResponseHandler listNodesResponseHandler() {
        return new ListNodesResponseHandler(messageErrorTransformer(), requestResponseMatcher());
    }

    @Bean
    ListStorageResponseHandler listStorageResponseHandler() {
        return new ListStorageResponseHandler(messageErrorTransformer());
    }

    @Bean
    RequestResponseMatcher requestResponseMatcher() {
        return new RequestResponseMatcher();
    }

    @Bean
    VCenterDiscoverResponseHandler vCenterDiscoverResponseHandler() {
        return new VCenterDiscoverResponseHandler(messageErrorTransformer(), discoveryInfoToVCenterDomainTransformer());
    }

    @Bean
    VCenterDestroyVmResponseHandler vCenterDestroyVmResponseHandler() {
        return new VCenterDestroyVmResponseHandler(messageErrorTransformer());
    }

    @Bean
    VCenterConsulRegisterResponseHandler vCenterConsulRegisterResponseHandler() {
        return new VCenterConsulRegisterResponseHandler(messageErrorTransformer());
    }

    @Bean
    VCenterHostPowerOperationResponseHandler vCenterHostPowerOperationResponseHandler() {
        return new VCenterHostPowerOperationResponseHandler(messageErrorTransformer());
    }

    @Bean
    vCenterHostMaintenanceModeResponseHandler vCenterHostMaintenanceModeResponseHandler() {
        return new vCenterHostMaintenanceModeResponseHandler(messageErrorTransformer());
    }

    @Bean
    VCenterClusterOperationsResponseHandler vCenterClusterOperationsResponseHandler() {
        return new VCenterClusterOperationsResponseHandler(messageErrorTransformer());
    }

    @Bean
    CoprHDConsulRegisterResponseHandler coprHDConsulRegisterResponseHandler() {
        return new CoprHDConsulRegisterResponseHandler(messageErrorTransformer());
    }

    @Bean
    ScaleIOOrderAckResponseHandler scaleIOOrderAckResponseHandler() {
        return new ScaleIOOrderAckResponseHandler(messageErrorTransformer());
    }

    @Bean
    VCenterTaskAckResponseHandler vCenterTaskAckResponseHandler()
    {
        return new VCenterTaskAckResponseHandler(messageErrorTransformer());
    }

    @Bean
    ScaleIORemoveResponseHandler scaleIORemoveResponseHandler() {
        return new ScaleIORemoveResponseHandler(messageErrorTransformer());
    }

    ErrorTransformer<HasMessageProperties<?>> messageErrorTransformer() {
        return new DefaultErrorTransformer<>(RabbitConfig.EXCHANGE_FRU_RESPONSE, replyTo,
                () -> new FruErrorMessage().withMessageProperties(new MessageProperties()), Error::new);
    }

    @Bean
    Advice retryPolicyAdvice() {
        MessageRecoverer messageRecoverer = new DefaultMessageRecoverer(rabbitTemplate);
        return new DefaultRetryPolicyAdvice(messageRecoverer, retryPolicy());
    }

    @Bean
    RetryPolicy retryPolicy() {
        return new DefaultRetryPolicy();
    }

    @Bean
    DiscoveryInfoToVCenterDomainTransformer discoveryInfoToVCenterDomainTransformer() {
        return new DiscoveryInfoToVCenterDomainTransformer();
    }

    @Bean
    HostListToHostRepresentationTransformer hostListToHostRepresentationTransformer()
    {
        return new HostListToHostRepresentationTransformer();
    }

    @Bean
    SDSListDtoToRemoveScaleIOMessageTransformer sdsListDtoToRemoveScaleIOMessageTransformer()
    {
        return new SDSListDtoToRemoveScaleIOMessageTransformer();
    }

    @Bean
    DestroyVMDtoToDestroyVMRequestMessageTransformer destroyVMDtoToDestroyVMRequestMessageTransformer()
    {
        return new DestroyVMDtoToDestroyVMRequestMessageTransformer();
    }

    @Bean
    ScaleIORestToScaleIODomainTransformer scaleIORestToScaleIODomainTransformer()
    {
        return new ScaleIORestToScaleIODomainTransformer();
    }
}
