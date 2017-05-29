/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;

import com.dell.cpsd.paqx.fru.amqp.model.FruErrorMessage;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesRequest;
import com.dell.cpsd.paqx.fru.amqp.model.ListNodesResponse;
import com.dell.cpsd.storage.capabilities.api.*;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.ConsulRegisterRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.ConsulRegisterResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryRequestInfoMessage;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeResponseMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationResponseMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the RabbitMQ artifacts used by the service.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Configuration
public class RabbitConfig {
    public static final String QUEUE_GENERAL_RESPONSE = "queue.dell.cpsd.fru-paqx.response";
    public static final String EXCHANGE_FRU_RESPONSE = "exchange.dell.cpsd.fru.paqx.response";

    // FRU PAQX response
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);
    private static final int MAX_ATTEMPTS = 10;
    private static final int INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER = 2.0;
    private static final int MAX_INTERVAL = 50000;

    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory rabbitConnectionFactory;

    /**
     * The configuration properties for the service.
     */
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(messageConverter());
        template.setRetryTemplate(retryTemplate());
        return template;
    }

    @Bean
    RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        LOGGER.debug("Retry configuration: " + backOffPolicy + " / " + retryPolicy);

        return retryTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(classMapper());
        messageConverter.setCreateMessageIds(true);

        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        return messageConverter;
    }

    @Bean
    String hostName() {
        try {
            return System.getProperty("container.id");
        } catch (Exception e) {
            throw new RuntimeException("Unable to identify containerId", e);
        }
    }

    @Bean
    String replyTo() {
        return propertiesConfig.applicationName() + "." + hostName();
    }

    @Bean
    AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(rabbitConnectionFactory);
    }

    @Bean
    ClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> classMappings = new HashMap<>();
        List<Class<?>> messageClasses = new ArrayList<>();

        messageClasses.add(FruErrorMessage.class);
        messageClasses.add(ListNodesRequest.class);
        messageClasses.add(ListNodesResponse.class);

        messageClasses.add(ListStorageRequestMessage.class);
        messageClasses.add(ListStorageResponseMessage.class);

        messageClasses.add(DiscoveryRequestInfoMessage.class);
        messageClasses.add(DiscoveryResponseInfoMessage.class);

        messageClasses.add(ConsulRegisterRequestMessage.class);
        messageClasses.add(ConsulRegisterResponseMessage.class);

        messageClasses.add(SIONodeRemoveRequestMessage.class);
        messageClasses.add(SIONodeRemoveResponseMessage.class);

        messageClasses.add(OrderAckMessage.class);

        messageClasses.add(HostPowerOperationRequestMessage.class);
        messageClasses.add(HostPowerOperationResponseMessage.class);

        messageClasses.add(ClusterOperationRequestMessage.class);
        messageClasses.add(ClusterOperationResponseMessage.class);

        messageClasses.add(DestroyVMRequestMessage.class);
        messageClasses.add(DestroyVMResponseMessage.class);

        messageClasses.add(HostMaintenanceModeRequestMessage.class);
        messageClasses.add(HostMaintenanceModeResponseMessage.class);

        messageClasses.add(com.dell.cpsd.storage.capabilities.api.ConsulRegisterRequestMessage.class);
        messageClasses.add(com.dell.cpsd.storage.capabilities.api.ConsulRegisterResponseMessage.class);

        MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();
        messageAnnotationProcessor.process(classMappings::put, messageClasses);
        classMapper.setIdClassMapping(classMappings);

        return classMapper;
    }

    @Bean
    Queue responseQueue() {
        return new Queue(QUEUE_GENERAL_RESPONSE + "." + replyTo());
    }
}
