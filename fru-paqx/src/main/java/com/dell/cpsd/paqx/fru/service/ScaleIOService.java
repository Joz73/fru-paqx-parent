/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;


import com.dell.cpsd.paqx.fru.dto.ConsulRegistryResult;
import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.paqx.fru.valueobject.LongRunning;
import com.dell.cpsd.storage.capabilities.api.OrderAckMessage;
import com.dell.cpsd.storage.capabilities.api.OrderInfo;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface ScaleIOService {
    CompletableFuture<ScaleIOSystemDataRestRep> listStorage(final EndpointCredentials scaleIOCredentials);
    LongRunning<OrderAckMessage, OrderInfo> sioNodeRemove(final EndpointCredentials coprHDCredentials,
            final EndpointCredentials scaleIOMDMCredentials, final String jobId, final HostRepresentation hostRepresentation);
    CompletableFuture<ConsulRegistryResult> requestConsulRegistration(final EndpointCredentials scaleIOCredentials);
}
