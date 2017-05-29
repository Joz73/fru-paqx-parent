/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.paqx.fru.amqp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * The configuration for the compliance data service message consumer.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * @since SINCE-TBD
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:persistence.properties"),
        @PropertySource(value = "file:/opt/dell/cpsd/fru-paqx/conf/persistence-config.properties", ignoreResourceNotFound = true)})
public class PersistencePropertiesConfig
{
    //    /*
    //     * The logger for this class
    //     */
    //    private static final ILogger LOGGER =
    //            RCDSLoggingManager.getLogger(PersistencePropertiesConfig.class);

    @Autowired
    protected Environment environment;

    @Bean
    public String databasePassword()
    {
        return this.environment.getProperty("remote.dell.database.password", "");
    }

    @Bean
    public String databaseUsername()
    {
        return this.environment.getProperty("remote.dell.database.username", "");
    }

    @Bean
    public String databaseUrl()
    {
        return this.environment.getRequiredProperty("remote.dell.database.url");
    }

    @Bean
    public String databaseDriverClassName()
    {
        return this.environment.getRequiredProperty("remote.dell.database.driver.class.name");
    }

    @Bean
    public String hibernateDialect()
    {
        return this.environment.getRequiredProperty("hibernate.dialect");
    }

    @Bean
    public String hibernateShowSql()
    {
        return this.environment.getRequiredProperty("hibernate.show_sql");
    }

    @Bean
    public String hibernateHBM2DdlAuto()
    {
        return this.environment.getRequiredProperty("hibernate.hbm2ddl.auto");
    }

    @Bean
    public String hibernateNamingStrategy()
    {
        return this.environment.getRequiredProperty("hibernate.naming_strategy");
    }

    @Bean
    public String hibernateDefaultSchema()
    {
        return this.environment.getRequiredProperty("hibernate.default_schema");
    }
}
