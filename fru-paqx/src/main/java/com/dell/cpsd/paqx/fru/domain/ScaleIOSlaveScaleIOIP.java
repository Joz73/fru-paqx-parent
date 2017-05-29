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
@DiscriminatorValue("SLAVE")
public class ScaleIOSlaveScaleIOIP extends ScaleIOIP
{
    public ScaleIOSlaveScaleIOIP()
    {
    }
    public ScaleIOSlaveScaleIOIP(final String id2, final String s)
    {
        super(s);
    }
}
