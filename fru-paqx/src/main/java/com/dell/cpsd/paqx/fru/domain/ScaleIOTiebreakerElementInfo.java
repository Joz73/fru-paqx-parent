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
@DiscriminatorValue("ELEMENT_TIEBREAKER")
public class ScaleIOTiebreakerElementInfo extends ScaleIOSDSStatusElementInfo
{
    public ScaleIOTiebreakerElementInfo()
    {
    }
    public ScaleIOTiebreakerElementInfo(final String id9, final int i, final String version1, final String slave, final String s,
            final String s1)
    {
        super(id9, i, version1, slave, s, s1);
    }
}
