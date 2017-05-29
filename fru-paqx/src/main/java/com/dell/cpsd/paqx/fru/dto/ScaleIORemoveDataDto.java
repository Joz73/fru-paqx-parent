/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.dto;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class ScaleIORemoveDataDto
{
    private String scaleIOInterface;
    private String scaleIOVolumeName;
    private String scaleIOMdmHosts;
    private String scaleIOSdsHosts;
    private String scaleIOSdcHosts;

    public ScaleIORemoveDataDto(final String scaleIOInterface, final String scaleIOVolumeName, final String scaleIOMdmHosts,
                                final String scaleIOSdsHosts, final String scaleIOSdcHosts)
    {
        this.scaleIOInterface = scaleIOInterface;
        this.scaleIOVolumeName = scaleIOVolumeName;
        this.scaleIOMdmHosts = scaleIOMdmHosts;
        this.scaleIOSdsHosts = scaleIOSdsHosts;
        this.scaleIOSdcHosts = scaleIOSdcHosts;
    }

    public String getScaleIOInterface()
    {
        return scaleIOInterface;
    }

    public String getScaleIOVolumeName()
    {
        return scaleIOVolumeName;
    }

    public String getScaleIOMdmHosts()
    {
        return scaleIOMdmHosts;
    }

    public String getScaleIOSdsHosts()
    {
        return scaleIOSdsHosts;
    }

    public String getScaleIOSdcHosts()
    {
        return scaleIOSdcHosts;
    }
}
