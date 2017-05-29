/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@DiscriminatorValue("SECONDARY")
public class ScaleIOSecondaryMDMIP extends ScaleIOIP
{
    public ScaleIOSecondaryMDMIP()
    {
    }
    public ScaleIOSecondaryMDMIP(final String id, final String ip)
    {
        super(ip);
    }
}
