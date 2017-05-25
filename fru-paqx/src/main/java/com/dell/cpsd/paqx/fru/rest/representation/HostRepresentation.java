/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.rest.representation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class HostRepresentation
{
    private String hostname;
    private String serialNumber;
    private String mgmtIP;
    private String status;
    private String uuid;

    public HostRepresentation()
    {
    }

    public HostRepresentation(final String hostname, final String serialNumber, final String mgmtIP, final String status, final String uuid)
    {
        this.hostname = hostname;
        this.serialNumber = serialNumber;
        this.mgmtIP = mgmtIP;
        this.status = status;
        this.uuid = uuid;
    }

    public String getHostname()
    {
        return hostname;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public String getMgmtIP()
    {
        return mgmtIP;
    }

    public String getStatus()
    {
        return status;
    }

    public String getUuid()
    {
        return uuid;
    }
}
