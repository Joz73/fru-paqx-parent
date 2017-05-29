/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.rest.repository;

import com.dell.cpsd.paqx.fru.domain.Host;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.DestroyVMDto;
import com.dell.cpsd.paqx.fru.dto.ScaleIORemoveDto;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public interface DataServiceRepository
{
    @Transactional
    Long saveScaleIOData(UUID jobId, ScaleIOData data);

    @Transactional
    Long saveVCenterData(UUID jobId, VCenter data);

    @Transactional
    List<Host> getVCenterHosts(String jobId);

    @Transactional
    ScaleIORemoveDto getScaleIORemoveDtoForSelectedHost(String jobId, HostRepresentation selectedHost, String userName, String password);

    @Transactional
    List<DestroyVMDto> getDestroyVMDtos(String jobId, HostRepresentation selectedHost, String vCenterUserName, String vCenterPassword,
                                        String vCenterEndpoint);
}
