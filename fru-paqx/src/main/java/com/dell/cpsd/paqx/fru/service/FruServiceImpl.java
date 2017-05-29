/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.EndpointProperty;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryLookupManager;
import com.dell.cpsd.hdp.capability.registry.client.callback.ListCapabilityProvidersResponse;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FruServiceImpl implements FruService
{
    private static final Logger LOG = LoggerFactory.getLogger(FruServiceImpl.class);

    private final ICapabilityRegistryLookupManager capabilityRegistryLookupManager;
    private final AmqpAdmin                        amqpAdmin;
    private final Queue                            responseQueue;

    @Autowired
    public FruServiceImpl(final ICapabilityRegistryLookupManager capabilityRegistryLookupManager, final AmqpAdmin amqpAdmin,
            final Queue responseQueue)
    {
        this.capabilityRegistryLookupManager = capabilityRegistryLookupManager;
        this.amqpAdmin = amqpAdmin;
        this.responseQueue = responseQueue;
    }

    @Override
    public List<Capability> findMatchingCapabilities(final String requiredCapability)
            throws CapabilityRegistryException, ServiceTimeoutException
    {
        try
        {
            final ListCapabilityProvidersResponse listCapabilityProvidersResponse = capabilityRegistryLookupManager
                    .listCapabilityProviders(TimeUnit.SECONDS.toMillis(5));
            final List<CapabilityProvider> capabilityProviders = listCapabilityProvidersResponse.getResponse();
            final List<Capability> matchedCapabilities = new ArrayList<>();
            capabilityProviders.stream().filter(Objects::nonNull).forEach(capabilityProvider ->
            {
                final List<Capability> capabilities = capabilityProvider.getCapabilities();
                matchedCapabilities.addAll(capabilities.stream().filter(Objects::nonNull)
                        .filter(c -> requiredCapability.equalsIgnoreCase(c.getProfile())).collect(Collectors.toList()));
            });
            return matchedCapabilities;
        }
        catch (CapabilityRegistryException e)
        {
            LOG.error("Failed while looking up Capability Registry for {}", requiredCapability, e);
            throw new CapabilityRegistryException(e.getMessage());
        }
        catch (ServiceTimeoutException e)
        {
            LOG.error("Service timed out while querying Capability Registry");
            throw new ServiceTimeoutException(e.getMessage());
        }
    }

    @Override
    public Map<String, String> declareBinding(final Capability capability, final String replyTo)
    {
        LOG.debug("Found matching capability [{}]", capability.getProfile());
        final List<EndpointProperty> endpointProperties = capability.getProviderEndpoint().getEndpointProperties();
        final Map<String, String> amqpProperties = endpointProperties.stream()
                .collect(Collectors.toMap(EndpointProperty::getName, EndpointProperty::getValue));

        final TopicExchange responseExchange = new TopicExchange(amqpProperties.get("response-exchange"));
        final String responseRoutingKey = amqpProperties.get("response-routing-key").replace("{replyTo}", "." + replyTo);

        amqpAdmin.declareBinding(BindingBuilder.bind(responseQueue).to(responseExchange).with(responseRoutingKey));

        String requestAckRoutingKey = amqpProperties.get("request-ack-routing-key");
        if (requestAckRoutingKey != null)
        {
            if (!requestAckRoutingKey.isEmpty())
            {
                requestAckRoutingKey = requestAckRoutingKey.replace("{replyTo}", "." + replyTo);
                amqpAdmin.declareBinding(BindingBuilder.bind(responseQueue).to(responseExchange).with(requestAckRoutingKey));
                LOG.debug("Adding binding {} {}", responseExchange.getName(), requestAckRoutingKey);
            }
        }

        LOG.debug("Adding binding {} {}", responseExchange.getName(), responseRoutingKey);

        return amqpProperties;
    }
}

