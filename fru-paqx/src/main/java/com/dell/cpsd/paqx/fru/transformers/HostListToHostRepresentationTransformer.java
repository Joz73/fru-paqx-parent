/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.domain.Host;
import com.dell.cpsd.paqx.fru.domain.VirtualNic;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class HostListToHostRepresentationTransformer
{
    public List<HostRepresentation> transform(final List<Host> hostList)
    {
        if (hostList==null)
        {
            return null;
        }

        return hostList.stream().filter(Objects::nonNull).map(x->transformHost(x)).collect(Collectors.toList());
    }

    private HostRepresentation transformHost(final Host host)
    {
        if (host==null)
        {
            return null;
        }

        String managementIP=null;
        //Find device with vmk0 (as per our understanding of management ip)
        VirtualNic device = host.getVirtualNicList().stream().filter(Objects::nonNull)
                .filter(y -> y.getDevice() != null && y.getDevice().equals("vmk0")).findFirst().orElse(null);

        if (device!=null)
        {
            managementIP = device.getIp();
        }

        return new HostRepresentation(host.getName(),"abcd", managementIP, "OK", host.getId());

        //TODO: It should look something like this
        //return new HostRepresentation(x.getName(), x.getSerialNumber(), managementIP, x.getStatus());
    }
}
