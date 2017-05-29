/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.dto;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DestroyVMDto
{
    private String username;
    private String password;
    private String endpoint;
    private String VMUuid;

    public DestroyVMDto(final String vCenterUserName, final String vCenterPassword, final String vCenterEndpoint, final String vmid)
    {
        this.username=vCenterUserName;
        this.password = vCenterPassword;
        this.endpoint=vCenterEndpoint;
        this.VMUuid = vmid;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEndpoint()
    {
        return endpoint;
    }

    public String getVMUuid()
    {
        return VMUuid;
    }
}
