/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.ClusterDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DVPortSettingDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DVSSecurityPolicyDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DataCenterDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DatastoreDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DatastoreSummaryDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DistributedVirtualSwicthPortConnectionDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DistributedVirtualSwitchHostMemberConfigInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DistributedVirtualSwitchHostMemberDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DistributedVirtualSwitchHostMemberPnicBackingDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DistributedVirtualSwitchHostMemberPnicSpecDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DvSwitchDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.GuestInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.GuestNicInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostConfigInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostDnsConfigDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostIpConfigDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostIpRouteConfigDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostNetworkInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostNetworkPolicyDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostNetworkSecurityPolicyDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostSystemDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostVirtualNicDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostVirtualNicSpecDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostVirtualSwitchDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostVirtualSwitchSpecDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.NetworkDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.PhysicalNicDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.VMwareDVSConfigInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.VirtualMachineDto;
import com.dell.cpsd.virtualization.capabilities.api.Cluster;
import com.dell.cpsd.virtualization.capabilities.api.DVPortSetting;
import com.dell.cpsd.virtualization.capabilities.api.DVSSecurityPolicy;
import com.dell.cpsd.virtualization.capabilities.api.Datacenter;
import com.dell.cpsd.virtualization.capabilities.api.Datastore;
import com.dell.cpsd.virtualization.capabilities.api.DatastoreSummary;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import com.dell.cpsd.virtualization.capabilities.api.DistributedVirtualSwicthPortConnection;
import com.dell.cpsd.virtualization.capabilities.api.DistributedVirtualSwitchHostMember;
import com.dell.cpsd.virtualization.capabilities.api.DistributedVirtualSwitchHostMemberConfigInfo;
import com.dell.cpsd.virtualization.capabilities.api.DistributedVirtualSwitchHostMemberPnicBacking;
import com.dell.cpsd.virtualization.capabilities.api.DistributedVirtualSwitchHostMemberPnicSpec;
import com.dell.cpsd.virtualization.capabilities.api.DvSwitch;
import com.dell.cpsd.virtualization.capabilities.api.GuestInfo;
import com.dell.cpsd.virtualization.capabilities.api.GuestNicInfo;
import com.dell.cpsd.virtualization.capabilities.api.HostConfigInfo;
import com.dell.cpsd.virtualization.capabilities.api.HostDnsConfig;
import com.dell.cpsd.virtualization.capabilities.api.HostIpConfig;
import com.dell.cpsd.virtualization.capabilities.api.HostIpRouteConfig;
import com.dell.cpsd.virtualization.capabilities.api.HostNetworkInfo;
import com.dell.cpsd.virtualization.capabilities.api.HostNetworkPolicy;
import com.dell.cpsd.virtualization.capabilities.api.HostNetworkSecurityPolicy;
import com.dell.cpsd.virtualization.capabilities.api.HostSystem;
import com.dell.cpsd.virtualization.capabilities.api.HostVirtualNic;
import com.dell.cpsd.virtualization.capabilities.api.HostVirtualNicSpec;
import com.dell.cpsd.virtualization.capabilities.api.HostVirtualSwitch;
import com.dell.cpsd.virtualization.capabilities.api.HostVirtualSwitchSpec;
import com.dell.cpsd.virtualization.capabilities.api.Network;
import com.dell.cpsd.virtualization.capabilities.api.PhysicalNic;
import com.dell.cpsd.virtualization.capabilities.api.VMwareDVSConfigInfo;
import com.dell.cpsd.virtualization.capabilities.api.VirtualMachine;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DiscoveryInfoToVCenterSystemPropertiesTransformer {
    public vCenterSystemProperties transform(final DiscoveryResponseInfoMessage discoveryResponseInfoMessage) {
        if (discoveryResponseInfoMessage == null) {
            return null;
        }
        vCenterSystemProperties properties = new vCenterSystemProperties();
        properties.setDataCenters(transformDataCenters(discoveryResponseInfoMessage.getDatacenters()));
        return properties;
    }

    private List<DataCenterDto> transformDataCenters(final List<Datacenter> datacenters) {
        if (datacenters == null) {
            return null;
        }
        return datacenters.stream().filter(Objects::nonNull).map(x -> transformDataCenter(x)).collect(Collectors.toList());
    }

    private DataCenterDto transformDataCenter(final Datacenter datacenter) {
        if (datacenter == null) {
            return null;
        }
        DataCenterDto returnVal = new DataCenterDto();
        returnVal.setVms(transformVms(datacenter.getVms()));
        returnVal.setName(datacenter.getName());
        returnVal.setId(datacenter.getId());
        returnVal.setNetworks(transformNetworks(datacenter.getNetworks()));
        returnVal.setDvSwitches(transformDVSwitches(datacenter.getDvSwitches()));
        returnVal.setDatastores(transformDatastores(datacenter.getDatastores()));
        returnVal.setClusters(transformClusters(datacenter.getClusters()));
        return returnVal;

    }

    private List<ClusterDto> transformClusters(final Map<String,Cluster> clustersMap) {
        if (clustersMap == null) {
            return null;
        }

        Collection<Cluster> clusters =clustersMap.values();
        if (clusters==null)
        {
            return null;
        }
        return clusters.stream().filter(Objects::nonNull).map(x -> transformCluster(x)).collect(Collectors.toList());
    }

    private ClusterDto transformCluster(final Cluster cluster) {
        if (cluster == null) {
            return null;
        }
        ClusterDto returnVal = new ClusterDto();
        returnVal.setHosts(transformHostSystems(cluster.getHosts()));
        returnVal.setId(cluster.getId());
        returnVal.setName(cluster.getName());
        return returnVal;
    }

    private List<HostSystemDto> transformHostSystems(final Map<String,HostSystem> hostsMap) {
        if (hostsMap == null) {
            return null;
        }

        Collection<HostSystem> hosts =hostsMap.values();
        if (hosts==null)
        {
            return null;
        }
        return hosts.stream().filter(Objects::nonNull).map(x -> transformHostSystem(x)).collect(Collectors.toList());
    }

    private HostSystemDto transformHostSystem(final HostSystem hostSystem) {
        if (hostSystem == null) {
            return null;
        }
        HostSystemDto returnVal = new HostSystemDto();
        returnVal.setDatastoreIds(transformDataStoreIds(hostSystem.getDatastoreIds()));
        returnVal.setHostConfigInfo(transformHostConfigInfo(hostSystem.getHostConfigInfo()));
        returnVal.setId(hostSystem.getId());
        returnVal.setName(hostSystem.getName());
        returnVal.setNetworkIds(transformNetworkIds(hostSystem.getNetworkIds()));
        returnVal.setVmIds(transformVmIds(hostSystem.getVmIds()));

        return returnVal;
    }

    private HostConfigInfoDto transformHostConfigInfo(final HostConfigInfo hostConfigInfo) {
        if (hostConfigInfo == null) {
            return null;
        }
        HostConfigInfoDto returnVal = new HostConfigInfoDto();
        returnVal.setHostNetworkInfo(transformHostNetworkInfo(hostConfigInfo.getHostNetworkInfo()));
        return returnVal;
    }

    private HostNetworkInfoDto transformHostNetworkInfo(final HostNetworkInfo hostNetworkInfo) {
        if (hostNetworkInfo == null) {
            return null;
        }
        HostNetworkInfoDto returnVal = new HostNetworkInfoDto();
        returnVal.setHostDnsConfig(transformHostDnsConfig(hostNetworkInfo.getHostDnsConfig()));
        returnVal.setHostIpRouteConfig(transformHostIpRouteConfig(hostNetworkInfo.getHostIpRouteConfig()));
        returnVal.setPnics(transformPnics(hostNetworkInfo.getPnics()));
        returnVal.setVnics(transformVnics(hostNetworkInfo.getVnics()));
        returnVal.setVswitchs(transformVSwitches(hostNetworkInfo.getVswitchs()));
        return returnVal;
    }

    private List<HostVirtualSwitchDto> transformVSwitches(final List<HostVirtualSwitch> vswitchs) {
        if (vswitchs == null) {
            return null;
        }
        return vswitchs.stream().filter(Objects::nonNull).map(x -> transformVSwitch(x)).collect(Collectors.toList());
    }

    private HostVirtualSwitchDto transformVSwitch(final HostVirtualSwitch hostVirtualSwitch) {
        if (hostVirtualSwitch == null) {
            return null;
        }
        HostVirtualSwitchDto returnVal = new HostVirtualSwitchDto();
        returnVal.setHostVirtualSwitchSpec(transformHostVirtualSwitchSpec(hostVirtualSwitch.getHostVirtualSwitchSpec()));
        returnVal.setKey(hostVirtualSwitch.getKey());
        returnVal.setName(hostVirtualSwitch.getName());
        return returnVal;
    }

    private HostVirtualSwitchSpecDto transformHostVirtualSwitchSpec(final HostVirtualSwitchSpec hostVirtualSwitchSpec) {
        if (hostVirtualSwitchSpec == null) {
            return null;
        }
        HostVirtualSwitchSpecDto returnVal = new HostVirtualSwitchSpecDto();
        returnVal.setNetworkPolicy(transformHostNetworkPolicy(hostVirtualSwitchSpec.getHostNetworkPolicy()));
        return returnVal;
    }

    private HostNetworkPolicyDto transformHostNetworkPolicy(final HostNetworkPolicy hostNetworkPolicy) {
        if (hostNetworkPolicy == null) {
            return null;
        }
        HostNetworkPolicyDto returnVal = new HostNetworkPolicyDto();
        returnVal.setHostNetworkSecurityPolicy(transformHostNetworkSecurityPolicy(hostNetworkPolicy.getHostNetworkSecurityPolicy()));
        return returnVal;
    }

    private HostNetworkSecurityPolicyDto transformHostNetworkSecurityPolicy(final HostNetworkSecurityPolicy hostNetworkSecurityPolicy) {
        if (hostNetworkSecurityPolicy == null) {
            return null;
        }
        HostNetworkSecurityPolicyDto returnVal = new HostNetworkSecurityPolicyDto();
        returnVal.setAllowPromiscuous(hostNetworkSecurityPolicy.getAllowPromiscuous());
        return returnVal;
    }

    private List<HostVirtualNicDto> transformVnics(final List<HostVirtualNic> vnics) {
        if (vnics == null) {
            return null;
        }
        return vnics.stream().filter(Objects::nonNull).map(x -> transformVnic(x)).collect(Collectors.toList());
    }

    private HostVirtualNicDto transformVnic(final HostVirtualNic hostVirtualNic) {
        if (hostVirtualNic == null) {
            return null;
        }
        HostVirtualNicDto returnVal = new HostVirtualNicDto();
        returnVal.setDevice(hostVirtualNic.getDevice());
        returnVal.setHostVirtualNicSpec(transformHostVirtualNicSpec(hostVirtualNic.getHostVirtualNicSpec()));
        returnVal.setPort(hostVirtualNic.getPort());
        returnVal.setPortGroup(hostVirtualNic.getPortGroup());
        return returnVal;
    }

    private HostVirtualNicSpecDto transformHostVirtualNicSpec(final HostVirtualNicSpec hostVirtualNicSpec) {
        if (hostVirtualNicSpec == null) {
            return null;
        }
        HostVirtualNicSpecDto returnVal = new HostVirtualNicSpecDto();
        returnVal.setDistributedVirtualSwitchPortConnection(transformDVSPC(hostVirtualNicSpec.getDistributedVirtualSwicthPortConnection()));
        returnVal.setHostIpConfig(transformHostIpConfig(hostVirtualNicSpec.getHostIpConfig()));
        returnVal.setMac(hostVirtualNicSpec.getMac());
        return returnVal;
    }

    private DistributedVirtualSwicthPortConnectionDto transformDVSPC(
            final DistributedVirtualSwicthPortConnection distributedVirtualSwicthPortConnection) {
        if (distributedVirtualSwicthPortConnection == null) {
            return null;
        }
        DistributedVirtualSwicthPortConnectionDto returnVal = new DistributedVirtualSwicthPortConnectionDto();
        returnVal.setPortGroupId(distributedVirtualSwicthPortConnection.getPortGroupId());
        returnVal.setPortKey(distributedVirtualSwicthPortConnection.getPortKey());
        return returnVal;
    }

    private HostIpConfigDto transformHostIpConfig(final HostIpConfig hostIpConfig) {
        if (hostIpConfig == null) {
            return null;
        }
        HostIpConfigDto returnVal = new HostIpConfigDto();
        returnVal.setDhcp(hostIpConfig.getDhcp());
        returnVal.setIpAddress(hostIpConfig.getIpAddress());
        returnVal.setSubnetMask(hostIpConfig.getSubnetMask());
        return returnVal;
    }

    private List<PhysicalNicDto> transformPnics(final List<PhysicalNic> pnics) {
        if (pnics == null) {
            return null;
        }
        return pnics.stream().filter(Objects::nonNull).map(x -> transformPnic(x)).collect(Collectors.toList());
    }

    private PhysicalNicDto transformPnic(final PhysicalNic physicalNic) {
        if (physicalNic == null) {
            return null;
        }
        PhysicalNicDto returnVal = new PhysicalNicDto();
        returnVal.setDevice(physicalNic.getDevice());
        returnVal.setDriver(physicalNic.getDriver());
        returnVal.setMac(physicalNic.getMac());
        returnVal.setPci(physicalNic.getPci());
        return returnVal;
    }

    private HostIpRouteConfigDto transformHostIpRouteConfig(final HostIpRouteConfig hostIpRouteConfig) {
        if (hostIpRouteConfig == null) {
            return null;
        }
        HostIpRouteConfigDto returnVal = new HostIpRouteConfigDto();
        returnVal.setDefaultGateway(hostIpRouteConfig.getDefaultGateway());
        returnVal.setDefaultGatewayDevice(hostIpRouteConfig.getDefaultGatewayDevice());
        returnVal.setIpV6DefaultGateway(hostIpRouteConfig.getIpV6DefaultGateway());
        returnVal.setIpV6DefaultGatewayDevice(hostIpRouteConfig.getIpV6DefaultGatewayDevice());
        return returnVal;
    }

    private HostDnsConfigDto transformHostDnsConfig(final HostDnsConfig hostDnsConfig) {
        if (hostDnsConfig == null) {
            return null;
        }
        HostDnsConfigDto returnVal = new HostDnsConfigDto();
        returnVal.setDhcp(hostDnsConfig.getDhcp());
        returnVal.setDomainName(hostDnsConfig.getDomainName());
        returnVal.setHostName(hostDnsConfig.getHostName());
        returnVal.setIpAddresses(transformIpAddresses(hostDnsConfig.getIpAddresses()));
        returnVal.setSearchDomains(transformSearchDomains(hostDnsConfig.getSearchDomains()));
        return returnVal;
    }

    private List<String> transformSearchDomains(final List<String> searchDomains) {
        if (searchDomains == null) {
            return null;
        }
        return searchDomains.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> transformNetworkIds(final List<String> networkIds) {
        if (networkIds == null) {
            return null;

        }
        return networkIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> transformDataStoreIds(final List<String> datastoreIds) {
        if (datastoreIds == null) {
            return null;
        }

        return datastoreIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<DatastoreDto> transformDatastores(final Map<String,Datastore> datastoresMap) {
        if (datastoresMap == null) {
            return null;
        }

        Collection<Datastore> datastores =datastoresMap.values();
        if (datastores==null)
        {
            return null;
        }
        return datastores.stream().filter(Objects::nonNull).map(x -> transformDatastore(x)).collect(Collectors.toList());
    }

    private DatastoreDto transformDatastore(final Datastore datastore) {
        if (datastore == null) {
            return null;
        }
        DatastoreDto returnVal = new DatastoreDto();
        returnVal.setDatastoreSummary(transformDatastoreSummary(datastore.getDatastoreSummary()));
        returnVal.setHostIds(transformHostIds(datastore.getHostIds()));
        returnVal.setId(datastore.getId());
        returnVal.setName(datastore.getName());
        returnVal.setVmIds(transformVmIds(datastore.getVmIds()));
        return returnVal;

    }

    private DatastoreSummaryDto transformDatastoreSummary(final DatastoreSummary datastoreSummary) {
        if (datastoreSummary == null) {
            return null;
        }

        DatastoreSummaryDto returnVal = new DatastoreSummaryDto();
        returnVal.setType(datastoreSummary.getType());
        returnVal.setUrl(datastoreSummary.getUrl());
        return returnVal;
    }

    private List<DvSwitchDto> transformDVSwitches(final Map<String,DvSwitch> dvSwitchesMap) {
        if (dvSwitchesMap == null) {
            return null;
        }

        Collection<DvSwitch> dvSwitches =dvSwitchesMap.values();
        if (dvSwitches==null)
        {
            return null;
        }
        return dvSwitches.stream().filter(Objects::nonNull).map(x -> transformDVSwitch(x)).collect(Collectors.toList());
    }

    private DvSwitchDto transformDVSwitch(final DvSwitch dvSwitch) {
        if (dvSwitch == null) {
            return null;
        }
        DvSwitchDto returnVal = new DvSwitchDto();
        returnVal.setId(dvSwitch.getId());
        returnVal.setName(dvSwitch.getName());
        returnVal.setPortGroupIds(transformPortGroupIds(dvSwitch.getPortGroupids()));
        returnVal.setUuid(dvSwitch.getUuid());
        returnVal.setVMwareDVSConfigInfo(transformVMwareDVSConfigInfo(dvSwitch.getVMwareDVSConfigInfo()));
        return returnVal;
    }

    private VMwareDVSConfigInfoDto transformVMwareDVSConfigInfo(final VMwareDVSConfigInfo vMwareDVSConfigInfo) {
        if (vMwareDVSConfigInfo == null) {
            return null;
        }
        VMwareDVSConfigInfoDto returnVal = new VMwareDVSConfigInfoDto();
        returnVal.setDVPortSetting(transformDVPortSetting(vMwareDVSConfigInfo.getDVPortSetting()));
        returnVal.setHostMembers(transformHostMembers(vMwareDVSConfigInfo.getHostMembers()));
        return returnVal;

    }

    private List<DistributedVirtualSwitchHostMemberDto> transformHostMembers(final List<DistributedVirtualSwitchHostMember> hostMembers) {
        if (hostMembers == null) {
            return null;
        }
        return hostMembers.stream().filter(Objects::nonNull).map(x -> transformHostMember(x)).collect(Collectors.toList());
    }

    private DistributedVirtualSwitchHostMemberDto transformHostMember(
            final DistributedVirtualSwitchHostMember distributedVirtualSwitchHostMember) {
        if (distributedVirtualSwitchHostMember == null) {
            return null;
        }
        DistributedVirtualSwitchHostMemberDto returnVal = new DistributedVirtualSwitchHostMemberDto();
        returnVal.setDistributedVirtualSwitchHostMemberConfigInfo(
                transformHostMemberConfigInfo(distributedVirtualSwitchHostMember.getDistributedVirtualSwitchHostMemberConfigInfo()));
        return returnVal;
    }

    private DistributedVirtualSwitchHostMemberConfigInfoDto transformHostMemberConfigInfo(
            final DistributedVirtualSwitchHostMemberConfigInfo distributedVirtualSwitchHostMemberConfigInfo) {
        if (distributedVirtualSwitchHostMemberConfigInfo == null) {
            return null;
        }
        DistributedVirtualSwitchHostMemberConfigInfoDto returnVal = new DistributedVirtualSwitchHostMemberConfigInfoDto();
        returnVal.setDistributedVirtualSwitchHostMemberPnicBacking(transformDistributedVSHMPnicBacking(
                distributedVirtualSwitchHostMemberConfigInfo.getDistributedVirtualSwitchHostMemberPnicBacking()));
        returnVal.setHostId(distributedVirtualSwitchHostMemberConfigInfo.getHostId());
        return returnVal;
    }

    private DistributedVirtualSwitchHostMemberPnicBackingDto transformDistributedVSHMPnicBacking(
            final DistributedVirtualSwitchHostMemberPnicBacking distributedVirtualSwitchHostMemberPnicBacking) {
        if (distributedVirtualSwitchHostMemberPnicBacking == null) {
            return null;
        }
        DistributedVirtualSwitchHostMemberPnicBackingDto returnVal = new DistributedVirtualSwitchHostMemberPnicBackingDto();
        returnVal.setSpec(transformDVSHMPBSpecs(distributedVirtualSwitchHostMemberPnicBacking.getSpec()));
        return returnVal;
    }

    private List<DistributedVirtualSwitchHostMemberPnicSpecDto> transformDVSHMPBSpecs(
            final List<DistributedVirtualSwitchHostMemberPnicSpec> spec) {
        if (spec == null) {
            return null;
        }
        return spec.stream().filter(Objects::nonNull).map(x -> transformDVSHMPBSpec(x)).collect(Collectors.toList());
    }

    private DistributedVirtualSwitchHostMemberPnicSpecDto transformDVSHMPBSpec(
            final DistributedVirtualSwitchHostMemberPnicSpec distributedVirtualSwitchHostMemberPnicSpec) {
        if (distributedVirtualSwitchHostMemberPnicSpec == null) {
            return null;
        }
        DistributedVirtualSwitchHostMemberPnicSpecDto returnVal = new DistributedVirtualSwitchHostMemberPnicSpecDto();
        returnVal.setPnicDevice(distributedVirtualSwitchHostMemberPnicSpec.getPnicDevice());
        returnVal.setPortGroupId(distributedVirtualSwitchHostMemberPnicSpec.getPortGroupId());
        returnVal.setUplinkPortKey(distributedVirtualSwitchHostMemberPnicSpec.getUplinkPortkey());
        return returnVal;
    }

    private DVPortSettingDto transformDVPortSetting(final DVPortSetting dvPortSetting) {
        if (dvPortSetting == null) {
            return null;
        }
        DVPortSettingDto returnVal = new DVPortSettingDto();
        returnVal.setDVSSecurityPolicy(transformDVSSecurityPolicy(dvPortSetting.getDVSSecurityPolicy()));
        return returnVal;
    }

    private DVSSecurityPolicyDto transformDVSSecurityPolicy(final DVSSecurityPolicy dvsSecurityPolicy) {
        if (dvsSecurityPolicy == null) {
            return null;
        }
        DVSSecurityPolicyDto returnVal = new DVSSecurityPolicyDto();
        returnVal.setAllowPromicuous(dvsSecurityPolicy.getAllowPromicuous());
        return returnVal;
    }

    private List<NetworkDto> transformNetworks(final Map<String,Network> networksMap) {
        if (networksMap == null) {
            return null;
        }

        Collection<Network> networks =networksMap.values();
        if (networks==null)
        {
            return null;
        }
        return networks.stream().filter(Objects::nonNull).map(x -> transformNetwork(x)).collect(Collectors.toList());
    }

    private NetworkDto transformNetwork(final Network network) {
        if (network == null) {
            return null;
        }
        NetworkDto returnVal = new NetworkDto();
        returnVal.setHostIds(transformHostIds(network.getHostIds()));
        returnVal.setId(network.getId());
        returnVal.setName(network.getName());
        returnVal.setVmIds(transformVmIds(network.getVmIds()));
        return returnVal;
    }

    private List<String> transformVmIds(final List<String> vmIds) {

        if (vmIds == null) {
            return null;
        }
        return vmIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> transformHostIds(final List<String> hostIds) {
        if (hostIds == null) {
            return null;
        }
        return hostIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<VirtualMachineDto> transformVms(final Map<String,VirtualMachine> vmsMap) {
        if (vmsMap == null) {
            return null;
        }
        Collection<VirtualMachine> vms =vmsMap.values();
        if (vms==null)
        {
            return null;
        }
        return vms.stream().filter(Objects::nonNull).map(x -> transformVm(x)).collect(Collectors.toList());
    }

    private VirtualMachineDto transformVm(final VirtualMachine virtualMachine) {
        if (virtualMachine == null) {
            return null;
        }
        VirtualMachineDto returnVal = new VirtualMachineDto();
        returnVal.setGuestInfo(transformGuestInfo(virtualMachine.getGuestInfo()));
        returnVal.setId(virtualMachine.getId());
        returnVal.setName(virtualMachine.getName());
        return returnVal;
    }

    private GuestInfoDto transformGuestInfo(final GuestInfo guestInfo) {
        if (guestInfo == null) {
            return null;
        }
        GuestInfoDto returnVal = new GuestInfoDto();
        returnVal.setGuestFullName(guestInfo.getGuestFullName());
        returnVal.setGuestId(guestInfo.getGuestId());
        returnVal.setHostName(guestInfo.getHostName());
        returnVal.setGuestNicInfo(transformGuestNicInfo(guestInfo.getNet()));
        return returnVal;
    }

    private List<GuestNicInfoDto> transformGuestNicInfo(final List<GuestNicInfo> net) {
        if (net == null) {
            return null;
        }
        return net.stream().filter(Objects::nonNull).map(x -> transformGuestNicInfo(x)).collect(Collectors.toList());
    }

    private GuestNicInfoDto transformGuestNicInfo(final GuestNicInfo guestNicInfo) {
        if (guestNicInfo == null) {
            return null;
        }
        GuestNicInfoDto returnVal = new GuestNicInfoDto();
        returnVal.setConnected(guestNicInfo.getConnected());
        returnVal.setIpAddresses(transformIpAddresses(guestNicInfo.getIpAddresses()));
        returnVal.setMacAddress(guestNicInfo.getMacAddress());
        returnVal.setNetworkId(guestNicInfo.getNetworkId());
        return returnVal;

    }

    private List<String> transformIpAddresses(final List<String> ipAddresses) {
        if (ipAddresses == null) {
            return null;
        }
        return ipAddresses.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> transformPortGroupIds(final List<String> portGroupIds) {
        if (portGroupIds == null) {
            return null;
        }
        return portGroupIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}