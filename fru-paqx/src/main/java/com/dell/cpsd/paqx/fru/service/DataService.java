/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;


import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveRequestMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;

import java.util.List;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface DataService {
    FRUSystemData getData(UUID jobId);

    void saveScaleioData(final UUID jobId, final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep);

    void saveVcenterData(final UUID jobId, final VCenter vCenter);

    List<HostRepresentation> getVCenterHosts(String jobId);

    SIONodeRemoveRequestMessage getSDSHostsToRemoveFromHostRepresentation(String jobId, HostRepresentation selectedHost,
            final String mdmPassword, final String mdmUserName);

    List<DestroyVMRequestMessage> getDestroyVMRequestMessage(String jobId, HostRepresentation selectedHost, String vCenterEndpoint,
            String vCenterPassword, String vCenterUserName);
}
