/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
public abstract class ScaleIOSDSStatusElementInfo extends ScaleIOSDSElementInfo
{
    @Column(name = "SDS_ELEMENT_STATUS")
    private String status;

    public ScaleIOSDSStatusElementInfo()
    {
    }

    public ScaleIOSDSStatusElementInfo(final String id9, final int i, final String version1, final String slave, final String s,
            final String s1)
    {
        super(id9, i, version1, slave, s);
        this.status = s1;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(final String status)
    {
        this.status = status;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(status).toHashCode();
    }

    /**
     * For the sake of non-circular checks "equals" checks for relationship attributes must be checked
     * on only one side of the relationship. In the case of OneToMany relationships it will be done on
     * the "One" side (the one holding the List)
     *
     * @param other the object to compare to
     * @return true if their attributes are equal
     */
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof ScaleIOSDSStatusElementInfo))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOSDSStatusElementInfo toot = ((ScaleIOSDSStatusElementInfo) other);
        return super.equals(other) && new EqualsBuilder().append(status, toot.status).isEquals();
    }
}
