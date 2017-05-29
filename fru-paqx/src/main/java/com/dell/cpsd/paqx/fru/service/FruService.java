/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;

import java.util.List;
import java.util.Map;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public interface FruService
{
    List<Capability> findMatchingCapabilities(final String requiredCapability) throws CapabilityRegistryException, ServiceTimeoutException;

    Map<String, String> declareBinding(final Capability capability, final String replyTo);
}

