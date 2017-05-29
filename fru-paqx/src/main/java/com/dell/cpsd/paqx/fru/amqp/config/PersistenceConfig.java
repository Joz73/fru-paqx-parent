/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.paqx.fru.amqp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

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
@EnableTransactionManagement
@Import({PersistencePropertiesConfig.class})
public class PersistenceConfig
{
    @Autowired
    private PersistencePropertiesConfig propertiesConfig;

    public DriverManagerDataSource dataSource()
    {
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(propertiesConfig.databaseDriverClassName());
        source.setUrl(propertiesConfig.databaseUrl());
        source.setUsername(propertiesConfig.databaseUsername());
        source.setPassword(propertiesConfig.databasePassword());

        return source;
    }

    @Bean
    JpaTransactionManager transactionManager()
    {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory().getObject());
        return manager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setPackagesToScan("com.dell.cpsd.paqx.fru.domain");

        Properties properties = new Properties();
        properties.put("hibernate.dialect", propertiesConfig.hibernateDialect());
        properties.put("hibernate.show_sql", propertiesConfig.hibernateShowSql());
        properties.put("hibernate.hbm2ddl.auto", propertiesConfig.hibernateHBM2DdlAuto());
        properties.put("hibernate.naming_strategy", propertiesConfig.hibernateNamingStrategy());
        //properties.put("hibernate.default_schema", propertiesConfig.hibernateDefaultSchema());

        entityManagerFactory.setJpaProperties(properties);
        return entityManagerFactory;
    }

    public JpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter returnVal = new HibernateJpaVendorAdapter();
        returnVal.setDatabase(Database.H2);
        returnVal.setGenerateDdl(true);
        returnVal.setShowSql(true);

        return returnVal;
    }
}