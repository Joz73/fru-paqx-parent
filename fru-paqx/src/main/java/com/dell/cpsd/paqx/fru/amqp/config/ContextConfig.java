/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.service.common.client.context.ConsumerContextConfig;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.realm.CombinedRealm;
import org.apache.catalina.realm.RealmBase;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * This is the client context configuration for the FRU PAQX
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p/>
 *
 * @since 1.0
 */
@Configuration
public class ContextConfig extends ConsumerContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ContextConfig.class);
    private static final String CONSUMER_NAME = "fru-paqx";

    /**
     * ContextConfig constructor.
     *
     * @since 1.0
     */
    public ContextConfig() {
        super(CONSUMER_NAME, false);
    }

    @Bean
    /**
     * This container is required in order to implement the redirect from http 8080 to https 18443 in spring boot.
     * This means that http can continue to be used but will automatically redirect to https
     * The responses from FRU will be https regardless of the protocol/port used by the cli.
     */ public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }

            @Override
            /**
             * This is the method where ssl is configured in the tomcat container.
             * We want to override this in order to be able to take an encrypted-base64-encoded password from
             * application.properties and to decode+decrypt it and provide it to the Ssl object before ssl configuration begins.
             */ protected void configureSsl(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ContextConfig: servletContainer: encoded password = " + ssl.getKeyStorePassword());
                }

                byte[] decodedBytes = Base64.getDecoder().decode(ssl.getKeyStorePassword());

                ssl.setKeyStorePassword(new String(decodedBytes));
                super.configureSsl(protocol, ssl);
            }
        };

        return tomcat;
    }
}
