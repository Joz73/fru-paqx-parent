/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.dto;

import com.dell.cpsd.paqx.fru.rest.dto.vCenterSystemProperties;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.ClusterDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DataCenterDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.GuestNicInfoDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostSystemDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.HostVirtualNicDto;
import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.VirtualMachineDto;
import com.dell.cpsd.storage.capabilities.api.MasterDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSDCDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSDSDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSDSIPDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import com.dell.cpsd.storage.capabilities.api.SlavesDataRestRep;
import com.dell.cpsd.storage.capabilities.api.TieBreakersDataRestRep;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class FRUSystemData
{
    @XmlElement
    private ScaleIOSystemDataRestRep scaleIOData;

    @XmlElement
    private vCenterSystemProperties vCenterSystem;

    private Map<String, Mapping> mapping = new HashMap<>();

    public ScaleIOSystemDataRestRep getScaleIOData()
    {
        return scaleIOData;
    }

    public void setScaleIOData(final ScaleIOSystemDataRestRep scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public vCenterSystemProperties getvCenterSystem()
    {
        return vCenterSystem;
    }

    public void setvCenterSystem(final vCenterSystemProperties vCenterSystem)
    {
        this.vCenterSystem = vCenterSystem;
        //Do merge in here.
        //Tiebreakers map to vcenter VMs. tiebreakers are also in the sds list in scaleio.
        //Slaves map to vcenter VMs. slaves are also in the sds list in scaleio
        //Master maps to vcenter VMs.  master is also in the sds list in scaleio
        //SDC elements mapto vcenter hosts.

        //We can ignore the vcenter information in scaleIO's json because vcenter already has it,and more.
        //Sometimes both ip's are not present in vcenter (i.e. the ip's in slave/master/sds/tiebreaker on the scaleio side).
        //That's why we check all of them and if we find one then we return that mapping.
        //Sometimes none of the ip's on scaleio are in vcenter (e.g. the second slave in the scaleio data(175,238) but you
        //can see it maps to VM-50 in the vcenter data, from the name of it in vcenter (contains the management ip in the name).
        //Question: is it ok to map via name if there are no corresponding ip addresses in vcenter (not implemented here yet).

        //Tiebreakers
        for (TieBreakersDataRestRep tiebreaker : scaleIOData.getMdmClusterDataRestRep().getTieBreakers())
        {

            //Now check the ips
            for (String ip : tiebreaker.getIps())
            {
                addVMMapping(tiebreaker, ip, tiebreaker.getIps(), tiebreaker.getManagementIPs());
            }

            //Now check the ips
            for (String ip : tiebreaker.getManagementIPs())
            {
                addVMMapping(tiebreaker, ip, tiebreaker.getIps(), tiebreaker.getManagementIPs());
            }
        }

        //Slaves
        for (SlavesDataRestRep slave : scaleIOData.getMdmClusterDataRestRep().getSlaves())
        {
            //Now check the ips
            for (String ip : slave.getIps())
            {
                addVMMapping(slave, ip, slave.getIps(), slave.getManagementIPs());
            }

            //Now check the ips
            for (String ip : slave.getManagementIPs())
            {
                addVMMapping(slave, ip, slave.getIps(), slave.getManagementIPs());
            }
        }

        //Master
        MasterDataRestRep master = scaleIOData.getMdmClusterDataRestRep().getMasterDataRestRep();
        for (String ip : master.getIps())
        {
            addVMMapping(master, ip, master.getIps(), master.getManagementIPs());
        }

        for (String ip : master.getManagementIPs())
        {
            addVMMapping(master, ip, master.getIps(), master.getManagementIPs());
        }

        //SDC
        for (ScaleIOSDCDataRestRep sdc : scaleIOData.getSdcList())
        {
            addHostMapping(sdc, sdc.getSdcIp());
        }
    }

    private <T> void addVMMapping(T element, String ip, List<String> ips, List<String> managementIPs)
    {
        //Look forthe ip in the vms in vcenter
        for (DataCenterDto dataCenter : vCenterSystem.getDataCenters())
        {
            for (VirtualMachineDto vm : dataCenter.getVms())
            {
                for (GuestNicInfoDto guestInfo : vm.getGuestInfo().getGuestNicInfo())
                {
                    for (String guestIP : guestInfo.getIpAddresses())
                    {
                        //Then we have a match
                        if (guestIP.equals(ip))
                        {
                            //We want to map the vm to the tiebreaker (and to the sds?)
                            VMMapping<T> vmmapping = new VMMapping<>(element, vm, findIpsInSDSList(ips, managementIPs), scaleIOData);
                            mapping.put(ip, vmmapping);
                        }
                    }
                }

            }
        }
    }

    private ScaleIOSDSDataRestRep findIpsInSDSList(List<String> ips, List<String> managementIPs)
    {
        for (ScaleIOSDSDataRestRep sdsElement : scaleIOData.getSdsList())
        {
            for (ScaleIOSDSIPDataRestRep ipElement : sdsElement.getIpList())
            {
                for (String ip : ips)
                {
                    if (ip.equals(ipElement.getIp()))
                    {
                        return sdsElement;
                    }
                }

                for (String ip : managementIPs)
                {
                    if (ip.equals(ipElement.getIp()))
                    {
                        return sdsElement;
                    }
                }
            }
        }
        return null;
    }

    private void addHostMapping(ScaleIOSDCDataRestRep element, String ip)
    {
        //Look for the ip in the vms in vcenter
        for (DataCenterDto dataCenter : vCenterSystem.getDataCenters())
        {
            for (ClusterDto cluster : dataCenter.getClusters())
            {
                for (HostSystemDto host : cluster.getHosts())
                {
                    for (HostVirtualNicDto vnic : host.getHostConfigInfo().getHostNetworkInfo().getVnics())
                    {
                        if (ip.equals(vnic.getHostVirtualNicSpec().getHostIpConfig().getIpAddress()))
                        {
                            mapping.put(ip, new HostMapping(element, host, scaleIOData));
                        }
                    }
                }
            }
        }
    }
}

abstract class Mapping<T>
{
    T                        element;
    ScaleIOSystemDataRestRep scaleIOData;

    public Mapping(T element, ScaleIOSystemDataRestRep scaleIOData)
    {
        this.scaleIOData = scaleIOData;
        this.element = element;
    }
}

class VMMapping<T> extends Mapping<T>
{
    private final VirtualMachineDto     vm;
    private final ScaleIOSDSDataRestRep sds;

    public VMMapping(T element, final VirtualMachineDto vm, final ScaleIOSDSDataRestRep sds, final ScaleIOSystemDataRestRep scaleIOData)
    {
        super(element, scaleIOData);
        this.vm = vm;
        this.sds = sds;
    }
}

class HostMapping extends Mapping<ScaleIOSDCDataRestRep>
{
    private final HostSystemDto host;

    public HostMapping(final ScaleIOSDCDataRestRep element, final HostSystemDto host, final ScaleIOSystemDataRestRep scaleIOData)
    {
        super(element, scaleIOData);
        this.host = host;
    }
}
