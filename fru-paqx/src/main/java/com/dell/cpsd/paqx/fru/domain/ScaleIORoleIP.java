/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Table(name = "SCALEIO_IP_LIST")
public class ScaleIORoleIP
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IP_LIST_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDS_ROLE")
    private String role;

    @Column(name = "SDS_IP")
    private String ip;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOSDS sds;

    public ScaleIORoleIP()
    {
    }

    public ScaleIORoleIP(final String role, final String ip)
    {
        this.role = role;
        this.ip = ip;
    }

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(final String role)
    {
        this.role = role;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(final String ip)
    {
        this.ip = ip;
    }

    public void setSds(final ScaleIOSDS sds)
    {
        this.sds = sds;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(role).append(ip).toHashCode();
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
        if (!(other instanceof ScaleIORoleIP))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIORoleIP toot = ((ScaleIORoleIP) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(role, toot.role).append(ip, toot.ip).isEquals();
    }
}
