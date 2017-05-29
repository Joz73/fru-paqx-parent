/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQCachingConnectionFactory;
import com.dell.cpsd.common.rabbitmq.connectors.TLSConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Production configuration of AMQP
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Configuration
public class ProductionConfig {
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean
    @Qualifier("rabbitConnectionFactory")
    public ConnectionFactory productionCachingConnectionFactory() {
        final com.rabbitmq.client.ConnectionFactory connectionFactory;
        if (propertiesConfig.isSslEnabled())
        {
            connectionFactory = new TLSConnectionFactory(propertiesConfig);
        }
        else
        {
            connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        }
        return new RabbitMQCachingConnectionFactory(connectionFactory, propertiesConfig);
    }
}
