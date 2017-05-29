/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import com.rabbitmq.client.impl.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * The configuration for the client.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:META-INF/spring/fru-paqx/rabbitmq.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:/opt/dell/cpsd/fru-paqx/conf/rabbitmq.properties", ignoreResourceNotFound = true)})
@Qualifier("rabbitPropertiesConfig")
public class PropertiesConfig extends RabbitMQPropertiesConfig {
    @Autowired
    protected org.springframework.core.env.Environment environment;

    @Bean
    public String trustStorePassphrase(){
        return environment.getProperty("remote.dell.amqp.rabbitTrustStorePassphrase");
    }

    @Bean
    public String keyStorePassPhrase(){
        return environment.getProperty("remote.dell.amqp.rabbitKeyStorePassPhrase");
    }

    @Bean
    public String keyStorePath(){
        return environment.getProperty("remote.dell.amqp.rabbitKeyStorePath");
    }

    @Bean
    public String trustStorePath() {
        return environment.getProperty("remote.dell.amqp.rabbitTrustStorePath");
    }

    @Bean
    public Boolean isSslEnabled()
    {
        return Boolean.valueOf(environment.getRequiredProperty("remote.dell.amqp.rabbitIsSslEnabled"));
    }
}

