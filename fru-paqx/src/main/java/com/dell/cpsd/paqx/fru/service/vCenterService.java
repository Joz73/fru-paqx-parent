/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.rest.dto.VCenterHostPowerOperationStatus;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.ClusterOperationResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.DestroyVmResponse;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.HostMaintenanceModeResponse;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.paqx.fru.valueobject.LongRunning;
import com.dell.cpsd.virtualization.capabilities.api.TaskAckMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface vCenterService {

    CompletableFuture<VCenter> showSystem(final EndpointCredentials vcenterCredentials);

    CompletableFuture<ConsulRegistryResult> requestConsulRegistration(final EndpointCredentials vcenterCredentials);

    LongRunning<TaskAckMessage, VCenterHostPowerOperationStatus> requestHostPowerOff(final EndpointCredentials vcenterCredentials,
                                                                           final String hostname);

    LongRunning<TaskAckMessage, DestroyVmResponse> requestVmDeletion(final EndpointCredentials vcenterCredentials, final String jobId,
            final HostRepresentation hostRepresentation);

    LongRunning<TaskAckMessage, HostMaintenanceModeResponse> requestHostMaintenanceModeEnable(EndpointCredentials vcenterCredentials,
                                                                                    String hostname);

    LongRunning<TaskAckMessage, ClusterOperationResponse> requestHostRemoval(final EndpointCredentials vcenterCredentials, final String hostname);

    LongRunning<TaskAckMessage, ClusterOperationResponse> requestHostAddition(EndpointCredentials vcenterCredentials, String hostname, String clusterId, String hostUsername,
            String hostPassword);
}
