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
@DiscriminatorValue("ELEMENT_MASTER")
public class ScaleIOMasterElementInfo extends ScaleIOSDSElementInfo
{
    public ScaleIOMasterElementInfo()
    {
        super();
    }
    public ScaleIOMasterElementInfo(final String id, final int port, final String versionInfo, final String name, final String role)
    {
        super(id, port, versionInfo, name, role);
    }
}
