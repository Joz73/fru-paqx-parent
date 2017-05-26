/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryLookupManager;
import com.dell.cpsd.paqx.fru.amqp.consumer.handler.AsyncAcknowledgement;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.rest.dto.VCenterHostPowerOperationStatus;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.DestroyVmResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.HostMaintenanceModeResponse;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.paqx.fru.valueobject.LongRunning;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationRequest;
import com.dell.cpsd.virtualization.capabilities.api.ClusterOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.ConsulRegisterRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.Credentials;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryRequestInfoMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostMaintenanceModeRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.HostPowerOperationRequestMessage;
import com.dell.cpsd.virtualization.capabilities.api.MaintenanceModeRequest;
import com.dell.cpsd.virtualization.capabilities.api.MessageProperties;
import com.dell.cpsd.virtualization.capabilities.api.PowerOperationRequest;
import com.dell.cpsd.virtualization.capabilities.api.RegistrationInfo;
import com.dell.cpsd.virtualization.capabilities.api.TaskAckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class vCenterServiceImpl implements vCenterService
{
    private static final Logger LOG = LoggerFactory.getLogger(vCenterServiceImpl.class);

    private final ICapabilityRegistryLookupManager                      capabilityRegistryLookupManager;
    private final RabbitTemplate                                        rabbitTemplate;
    private final AmqpAdmin                                             amqpAdmin;
    private final Queue                                                 responseQueue;
    private final AsyncAcknowledgement<VCenter>                         vCenterAsyncAcknowledgement;
    private final AsyncAcknowledgement<ConsulRegistryResult>            consulRegisterAsyncAcknowledgement;
    private final AsyncAcknowledgement<DestroyVmResponse>               vmDeletionAsyncAcknowledgement;
    private final AsyncAcknowledgement<VCenterHostPowerOperationStatus> vCenterHostPowerAsyncAcknowledgement;
    private final AsyncAcknowledgement<HostMaintenanceModeResponse>     hostMaintenanceModeAsyncAcknowledgement;
    private final AsyncAcknowledgement<ClusterOperationResponse>        vcenterClusterOperationAsyncAcknowledgement;
    private final AsyncAcknowledgement<TaskAckMessage>                  vcenterTaskAckAsyncAcknowledgement;
    private final String                                                replyTo;
    private final FruService                                            fruService;
    private final DataService                                           dataService;

    @Autowired
    public vCenterServiceImpl(final ICapabilityRegistryLookupManager capabilityRegistryLookupManager, final RabbitTemplate rabbitTemplate,
            final AmqpAdmin amqpAdmin, final Queue responseQueue,
            @Qualifier(value = "vCenterDiscoverResponseHandler") final AsyncAcknowledgement<VCenter> vCenterAsyncAcknowledgement,
            @Qualifier(value = "vCenterConsulRegisterResponseHandler") final AsyncAcknowledgement<ConsulRegistryResult> consulRegisterAsyncAcknowledgement,
            @Qualifier(value = "vCenterDestroyVmResponseHandler") final AsyncAcknowledgement<DestroyVmResponse> vmDeletionAsyncAcknowledgement,
            @Qualifier(value = "vCenterHostPowerOperationResponseHandler") final AsyncAcknowledgement<VCenterHostPowerOperationStatus> vCenterHostPowerAsyncAcknowledgement,
            @Qualifier(value = "vCenterHostMaintenanceModeResponseHandler") final AsyncAcknowledgement<HostMaintenanceModeResponse> hostMaintenanceModeAsyncAcknowledgement,
            @Qualifier(value = "vCenterClusterOperationsResponseHandler") final AsyncAcknowledgement<ClusterOperationResponse> vcenterClusterOperationAsyncAcknowledgement,
            @Qualifier(value = "vCenterTaskAckResponseHandler") final AsyncAcknowledgement<TaskAckMessage> vcenterTaskAckAsyncAcknowledgement,
            @Qualifier(value = "replyTo") final String replyTo, final FruService fruService, final DataService dataService)
    {
        this.capabilityRegistryLookupManager = capabilityRegistryLookupManager;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.responseQueue = responseQueue;
        this.vCenterAsyncAcknowledgement = vCenterAsyncAcknowledgement;
        this.consulRegisterAsyncAcknowledgement = consulRegisterAsyncAcknowledgement;
        this.vmDeletionAsyncAcknowledgement = vmDeletionAsyncAcknowledgement;
        this.vCenterHostPowerAsyncAcknowledgement = vCenterHostPowerAsyncAcknowledgement;
        this.hostMaintenanceModeAsyncAcknowledgement = hostMaintenanceModeAsyncAcknowledgement;
        this.vcenterClusterOperationAsyncAcknowledgement = vcenterClusterOperationAsyncAcknowledgement;
        this.vcenterTaskAckAsyncAcknowledgement = vcenterTaskAckAsyncAcknowledgement;
        this.replyTo = replyTo;
        this.fruService = fruService;
        this.dataService = dataService;
    }

    public CompletableFuture<VCenter> showSystem(final EndpointCredentials vcenterCredentials)
    {
        final String requiredCapability = "vcenter-discover";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            final String correlationId = UUID.randomUUID().toString();
            final DiscoveryRequestInfoMessage requestMessage = new DiscoveryRequestInfoMessage();
            requestMessage.setMessageProperties(
                    new MessageProperties().withCorrelationId(correlationId).withReplyTo(replyTo).withTimestamp(new Date()));

            final Credentials credentials = new Credentials();
            credentials.setUsername(vcenterCredentials.getUsername());
            credentials.setAddress(vcenterCredentials.getEndpointUrl());
            credentials.setPassword(vcenterCredentials.getPassword());
            requestMessage.setCredentials(credentials);

            final CompletableFuture<VCenter> promise = vCenterAsyncAcknowledgement.register(correlationId);

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return promise;
        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return CompletableFuture.completedFuture(null);
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<VCenter> promise = new CompletableFuture<>();
            LOG.error("Malformed URL Exception with url [{}]", vcenterCredentials.getEndpointUrl());
            promise.completeExceptionally(e);
            return promise;
        }
    }

    public CompletableFuture<ConsulRegistryResult> requestConsulRegistration(final EndpointCredentials vcenterCredentials)
    {
        final String requiredCapability = "vcenter-consul-register";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return CompletableFuture.completedFuture(null);
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            final String correlationId = UUID.randomUUID().toString();
            ConsulRegisterRequestMessage requestMessage = new ConsulRegisterRequestMessage();
            requestMessage.setMessageProperties(
                    new MessageProperties().withCorrelationId(correlationId).withReplyTo(replyTo).withTimestamp(new Date()));

            final RegistrationInfo registrationInfo = new RegistrationInfo(vcenterCredentials.getEndpointUrl(),
                    vcenterCredentials.getPassword(), vcenterCredentials.getUsername());
            requestMessage.setRegistrationInfo(registrationInfo);

            final CompletableFuture<ConsulRegistryResult> promise = consulRegisterAsyncAcknowledgement.register(correlationId);

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return promise;
        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return CompletableFuture.completedFuture(null);
        }
        catch (MalformedURLException e)
        {
            final CompletableFuture<ConsulRegistryResult> promise = new CompletableFuture<>();
            LOG.error("Malformed URL Exception with url [{}]", vcenterCredentials.getEndpointUrl());
            promise.completeExceptionally(e);
            return promise;
        }
    }

    @Override
    public LongRunning<TaskAckMessage, DestroyVmResponse> requestVmDeletion(final EndpointCredentials vcenterCredentials,
            final String jobId, final HostRepresentation hostRepresentation)
    {
        final String requiredCapability = "vcenter-destroy-virtualMachine";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();

            final List<DestroyVMRequestMessage> requestMessages = dataService
                    .getDestroyVMRequestMessage(jobId, hostRepresentation, vcenterCredentials.getEndpointUrl(),
                            vcenterCredentials.getPassword(), vcenterCredentials.getUsername());

            if (requestMessages != null && !requestMessages.isEmpty())
            {
                final String requestExchange = amqpProperties.get("request-exchange");
                final String requestRoutingKey = amqpProperties.get("request-routing-key");

                final DestroyVMRequestMessage requestMessage = requestMessages.get(0);
                requestMessage.setMessageProperties(new MessageProperties(new Date(), correlationId, replyTo));

                final CompletableFuture<TaskAckMessage> acknowledgementPromise = vcenterTaskAckAsyncAcknowledgement.register(correlationId);
                final CompletableFuture<DestroyVmResponse> completionPromise = vmDeletionAsyncAcknowledgement.register(correlationId);

                rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

                return new LongRunning<>(acknowledgementPromise, completionPromise);
            }

            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (MalformedURLException e)
        {
            LOG.error("Malformed URL Exception occurred for Endpoint URL: [{}]", vcenterCredentials.getEndpointUrl());
            final CompletableFuture<TaskAckMessage> acknowledgementPromise = new CompletableFuture<>();
            acknowledgementPromise.completeExceptionally(e);
            final CompletableFuture<DestroyVmResponse> completionPromise = new CompletableFuture<>();
            completionPromise.completeExceptionally(e);
            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
    }

    @Override
    public LongRunning<TaskAckMessage, HostMaintenanceModeResponse> requestHostMaintenanceModeEnable(
            final EndpointCredentials vcenterCredentials, final String hostname)
    {
        final String requiredCapability = "vcenter-enterMaintenance";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String correlationId = UUID.randomUUID().toString();
            final HostMaintenanceModeRequestMessage requestMessage = new HostMaintenanceModeRequestMessage();

            final MessageProperties messageProperties = new MessageProperties(new Date(), correlationId, replyTo);
            requestMessage.setMessageProperties(messageProperties);
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));

            MaintenanceModeRequest maintenanceModeRequest = new MaintenanceModeRequest(hostname, Boolean.TRUE);
            requestMessage.setMaintenanceModeRequest(maintenanceModeRequest);

            final CompletableFuture<TaskAckMessage> acknowledgementPromise = vcenterTaskAckAsyncAcknowledgement.register(correlationId);
            final CompletableFuture<HostMaintenanceModeResponse> completionPromise = hostMaintenanceModeAsyncAcknowledgement
                    .register(correlationId);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (MalformedURLException e)
        {
            LOG.error("Malformed URL Exception occurred for Endpoint URL: [{}]", vcenterCredentials.getEndpointUrl());
            final CompletableFuture<TaskAckMessage> acknowledgementPromise = new CompletableFuture<>();
            acknowledgementPromise.completeExceptionally(e);
            final CompletableFuture<HostMaintenanceModeResponse> completionPromise = new CompletableFuture<>();
            completionPromise.completeExceptionally(e);
            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
    }

    @Override
    public LongRunning<TaskAckMessage, VCenterHostPowerOperationStatus> requestHostPowerOff(final EndpointCredentials vcenterCredentials,
            final String hostname)
    {
        final String requiredCapability = "vcenter-powercommand";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            final String correlationId = UUID.randomUUID().toString();
            final HostPowerOperationRequestMessage requestMessage = new HostPowerOperationRequestMessage();
            requestMessage.setMessageProperties(
                    new MessageProperties().withCorrelationId(correlationId).withReplyTo(replyTo).withTimestamp(new Date()));

            final Credentials credentials = new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername());
            requestMessage.setCredentials(credentials);

            final PowerOperationRequest powerOperationRequest = new PowerOperationRequest(hostname,
                    PowerOperationRequest.PowerOperation.POWER_OFF);

            requestMessage.setPowerOperationRequest(powerOperationRequest);

            final CompletableFuture<TaskAckMessage> acknowledgementPromise = vcenterTaskAckAsyncAcknowledgement.register(correlationId);
            final CompletableFuture<VCenterHostPowerOperationStatus> completionPromise = vCenterHostPowerAsyncAcknowledgement
                    .register(correlationId);

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (MalformedURLException e)
        {
            LOG.error("Malformed URL Exception occurred for Endpoint URL: [{}]", vcenterCredentials.getEndpointUrl());
            final CompletableFuture<TaskAckMessage> acknowledgementPromise = new CompletableFuture<>();
            acknowledgementPromise.completeExceptionally(e);
            final CompletableFuture<VCenterHostPowerOperationStatus> completionPromise = new CompletableFuture<>();
            completionPromise.completeExceptionally(e);
            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
    }

    @Override
    public LongRunning<TaskAckMessage, ClusterOperationResponse> requestHostRemoval(final EndpointCredentials vcenterCredentials, final String hostname)
    {
        final String requiredCapability = "vcenter-remove-host";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            final String correlationId = UUID.randomUUID().toString();
            final ClusterOperationRequestMessage requestMessage = new ClusterOperationRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));

            final ClusterOperationRequest clusterOperationRequest = new ClusterOperationRequest();
            clusterOperationRequest.setHostName(hostname);
            clusterOperationRequest.setClusterOperation(ClusterOperationRequest.ClusterOperation.REMOVE_HOST);
            requestMessage.setClusterOperationRequest(clusterOperationRequest);

            final CompletableFuture<TaskAckMessage> acknowledgementPromise = vcenterTaskAckAsyncAcknowledgement.register(correlationId);
            final CompletableFuture<ClusterOperationResponse> completionPromise = vcenterClusterOperationAsyncAcknowledgement
                    .register(correlationId);

            LOG.info("Host removal request with correlation id [{}]", correlationId);

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return new LongRunning<>(acknowledgementPromise, completionPromise);

        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (MalformedURLException e)
        {
            LOG.error("Malformed URL Exception occurred for operation Host Removal with Endpoint URL: [{}]",
                    vcenterCredentials.getEndpointUrl());
            final CompletableFuture<TaskAckMessage> acknowledgementPromise = new CompletableFuture<>();
            acknowledgementPromise.completeExceptionally(e);
            final CompletableFuture<ClusterOperationResponse> completionPromise = new CompletableFuture<>();
            completionPromise.completeExceptionally(e);
            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
    }

    @Override
    public LongRunning<TaskAckMessage, ClusterOperationResponse> requestHostAddition(final EndpointCredentials vcenterCredentials,
            final String hostname, final String clusterId, final String hostUsername, final String hostPassword)
    {
        final String requiredCapability = "vcenter-addhostvcenter";

        try
        {
            new URL(vcenterCredentials.getEndpointUrl());

            final List<Capability> matchedCapabilities = fruService.findMatchingCapabilities(requiredCapability);
            if (matchedCapabilities.isEmpty())
            {
                LOG.info("No matching capability found for capability [{}]", requiredCapability);
                return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
            }
            final Capability matchedCapability = matchedCapabilities.stream().findFirst().get();
            LOG.debug("Found capability {}", matchedCapability.getProfile());

            final Map<String, String> amqpProperties = fruService.declareBinding(matchedCapability, replyTo);

            final String requestExchange = amqpProperties.get("request-exchange");
            final String requestRoutingKey = amqpProperties.get("request-routing-key");

            final String correlationId = UUID.randomUUID().toString();

            final ClusterOperationRequestMessage requestMessage = new ClusterOperationRequestMessage();
            requestMessage.setCredentials(new Credentials(vcenterCredentials.getEndpointUrl(), vcenterCredentials.getPassword(),
                    vcenterCredentials.getUsername()));
            final ClusterOperationRequest clusterOperationRequest = new ClusterOperationRequest();
            clusterOperationRequest.setHostName(hostname);
            clusterOperationRequest.setClusterID(clusterId);
            clusterOperationRequest.setUserName(hostUsername);
            clusterOperationRequest.setPassword(hostPassword);
            clusterOperationRequest.setClusterOperation(ClusterOperationRequest.ClusterOperation.ADD_HOST);
            requestMessage.setClusterOperationRequest(clusterOperationRequest);

            final CompletableFuture<TaskAckMessage> acknowledgementPromise = vcenterTaskAckAsyncAcknowledgement.register(correlationId);
            final CompletableFuture<ClusterOperationResponse> completionPromise = vcenterClusterOperationAsyncAcknowledgement.register(correlationId);

            LOG.info("Host addition request with correlation id [{}]", correlationId);

            rabbitTemplate.convertAndSend(requestExchange, requestRoutingKey, requestMessage);

            return new LongRunning<>(acknowledgementPromise, completionPromise);

        }
        catch (ServiceTimeoutException | CapabilityRegistryException e)
        {
            return new LongRunning<>(CompletableFuture.completedFuture(null), CompletableFuture.completedFuture(null));
        }
        catch (MalformedURLException e)
        {
            LOG.error("Malformed URL Exception occurred for operation Host Addition with Endpoint URL: [{}]",
                    vcenterCredentials.getEndpointUrl());
            final CompletableFuture<TaskAckMessage> acknowledgementPromise = new CompletableFuture<>();
            acknowledgementPromise.completeExceptionally(e);
            final CompletableFuture<ClusterOperationResponse> completionPromise = new CompletableFuture<>();
            completionPromise.completeExceptionally(e);
            return new LongRunning<>(acknowledgementPromise, completionPromise);
        }
    }
}
