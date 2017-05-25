/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "SCALEIO_IP")
public class ScaleIOIP
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IP_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "IP")
    private String ip;

    @Column(name = "SCALEIO_IP_TYPE")
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    public ScaleIOData scaleIOData;

    @ManyToOne(cascade = CascadeType.ALL)
    public ScaleIOSDSElementInfo sdsElementInfo;

    public ScaleIOIP()
    {

    }

    public ScaleIOIP(final String ip)
    {
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

    public String getIp()
    {
        return ip;
    }

    public void setIp(final String ip)
    {
        this.ip = ip;
    }

    public String getType()
    {
        return type;
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    public ScaleIOData getScaleIOData()
    {
        return this.scaleIOData;
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(ip).toHashCode();
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
        if (!(other instanceof ScaleIOIP))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOIP toot = ((ScaleIOIP) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(ip, toot.ip).isEquals();
    }

    public void setScaleIOSDSElementInfo(final ScaleIOSDSElementInfo sdsElementInfo)
    {
        this.sdsElementInfo = sdsElementInfo;
    }
}

