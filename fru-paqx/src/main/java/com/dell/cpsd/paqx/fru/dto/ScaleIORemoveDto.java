/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.dto;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class ScaleIORemoveDto
{
    private       String               ipAddress;
    private final String               userName;
    private final String               password;
    private final int                  portFromEndpoint;
    private final int                  serviceAPIPort;
    private       ScaleIORemoveDataDto scaleIORemoveDataDto;

    public ScaleIORemoveDto(final String userName, final String password, final int portFromEndpoint, final int serviceAPIPort, String ipAddress)
    {
        this.userName = userName;
        this.password = password;
        this.portFromEndpoint = portFromEndpoint;
        this.serviceAPIPort = serviceAPIPort;
        this.ipAddress=ipAddress;
    }

    public void setScaleIORemoveDataDto(final ScaleIORemoveDataDto scaleIORemoveDataDto)
    {
        this.scaleIORemoveDataDto = scaleIORemoveDataDto;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public int getPortFromEndpoint()
    {
        return portFromEndpoint;
    }

    public int getServiceAPIPort()
    {
        return serviceAPIPort;
    }

    public ScaleIORemoveDataDto getScaleIORemoveDataDto()
    {
        return scaleIORemoveDataDto;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }
}
