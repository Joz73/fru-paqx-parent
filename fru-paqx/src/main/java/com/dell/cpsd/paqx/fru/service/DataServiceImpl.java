/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.domain.Host;
import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.paqx.fru.dto.DestroyVMDto;
import com.dell.cpsd.paqx.fru.dto.FRUSystemData;
import com.dell.cpsd.paqx.fru.dto.ScaleIORemoveDto;
import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.repository.DataServiceRepository;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;
import com.dell.cpsd.paqx.fru.transformers.DestroyVMDtoToDestroyVMRequestMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterDomainTransformer;
import com.dell.cpsd.paqx.fru.transformers.HostListToHostRepresentationTransformer;
import com.dell.cpsd.paqx.fru.transformers.SDSListDtoToRemoveScaleIOMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveRequestMessage;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class DataServiceImpl implements DataService {

    private final Map<UUID, FRUSystemData> jobIdFRUSystemData = new HashMap<>();

    @Autowired
    DataServiceRepository repository;

    @Autowired
    ScaleIORestToScaleIODomainTransformer scaleIORestToScaleIODomainTransformer;

    @Autowired
    private DiscoveryInfoToVCenterDomainTransformer discoveryInfoToVCenterDomainTransformer;

    @Autowired
    HostListToHostRepresentationTransformer hostListToHostRepresentationTransformer;

    @Autowired
    private SDSListDtoToRemoveScaleIOMessageTransformer sdsListDtoToRemoveScaleIOMessageTransformer;

    @Autowired
    private DestroyVMDtoToDestroyVMRequestMessageTransformer destroyVMDtoToDestroyVMRequestMessageTransformer;

    @Autowired
    public DataServiceImpl(DataServiceRepository repository, ScaleIORestToScaleIODomainTransformer scaleIORestToScaleIODomainTransformer, DiscoveryInfoToVCenterDomainTransformer discoveryInfoToVCenterDomainTransformer,
            HostListToHostRepresentationTransformer hostListToHostRepresentationTransformer, SDSListDtoToRemoveScaleIOMessageTransformer sdsListDtoToRemoveScaleIOMessageTransformer, DestroyVMDtoToDestroyVMRequestMessageTransformer destroyVMDtoToDestoryVMRequestMessageTransformer)
    {
        this.repository=repository;
        this.scaleIORestToScaleIODomainTransformer=scaleIORestToScaleIODomainTransformer;
        this.discoveryInfoToVCenterDomainTransformer=discoveryInfoToVCenterDomainTransformer;
        this.hostListToHostRepresentationTransformer=hostListToHostRepresentationTransformer;
        this.sdsListDtoToRemoveScaleIOMessageTransformer = sdsListDtoToRemoveScaleIOMessageTransformer;
        this.destroyVMDtoToDestroyVMRequestMessageTransformer=destroyVMDtoToDestoryVMRequestMessageTransformer;
    }

    @Override
    public FRUSystemData getData(UUID jobId) {
        return ensureSystemDataExists(jobId);
    }

    @Override
    public void saveScaleioData(final UUID jobId, final ScaleIOSystemDataRestRep scaleIOSystemDataRestRep) {
        ScaleIOData data = scaleIORestToScaleIODomainTransformer.transform(scaleIOSystemDataRestRep);
        repository.saveScaleIOData(jobId,data);
        FRUSystemData fruSystemData = ensureSystemDataExists(jobId);
        fruSystemData.setScaleIOData(scaleIOSystemDataRestRep);
    }

    @Override
    public void saveVcenterData(final UUID jobId, final VCenter vCenter) {
        repository.saveVCenterData(jobId,vCenter);
    }

    @Override
    public List<HostRepresentation> getVCenterHosts(String jobId)
    {
        List<Host> hostList=repository.getVCenterHosts(jobId);
        return hostListToHostRepresentationTransformer.transform(hostList);
    }

    @Override
    public SIONodeRemoveRequestMessage getSDSHostsToRemoveFromHostRepresentation(String jobId, HostRepresentation selectedHost,
            final String mdmPassword, final String mdmUserName)
    {
       ScaleIORemoveDto hostList=repository.getScaleIORemoveDtoForSelectedHost(jobId, selectedHost, mdmUserName, mdmPassword);
        return sdsListDtoToRemoveScaleIOMessageTransformer.transform(hostList);
    }

    @Override
    public List<DestroyVMRequestMessage> getDestroyVMRequestMessage(String jobId, HostRepresentation selectedHost,
            final String vCenterEndpoint, final String vCenterPassword, final String vCenterUserName)
    {
        List<DestroyVMDto> destroyVMDtos=repository.getDestroyVMDtos(jobId, selectedHost, vCenterUserName, vCenterPassword, vCenterEndpoint);
        return destroyVMDtoToDestroyVMRequestMessageTransformer.transform(destroyVMDtos);
    }

    private FRUSystemData ensureSystemDataExists(UUID jobId) {
        FRUSystemData fruSystemData = jobIdFRUSystemData.get(jobId);
        if (fruSystemData == null) {
            fruSystemData = new FRUSystemData();
            jobIdFRUSystemData.put(jobId, fruSystemData);
        }
        return fruSystemData;
    }
}
