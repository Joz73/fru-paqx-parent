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
public class ScaleIOMasterScaleIOIP extends com.dell.cpsd.paqx.fru.domain.ScaleIOIP
{
    public ScaleIOMasterScaleIOIP()
    {
        super();
    }
    public ScaleIOMasterScaleIOIP(final String id2, final String s)
    {
        super(s);
    }
}
