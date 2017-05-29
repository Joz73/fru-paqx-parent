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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Table(name = "SCALEIO_SDS")
public class ScaleIOSDS
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SDS_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDS_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "SDS_NAME")
    private String name;

    @Column(name = "SDS_STATE")
    private String sdsState;

    @Column(name = "SDS_PORT")
    private int port;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOFaultSet faultSet;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOProtectionDomain protectionDomain;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sds")
    private List<ScaleIORoleIP> roleIPs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sds")
    private List<ScaleIODevice> devices = new ArrayList<>();

    public ScaleIOSDS()
    {
    }
    public ScaleIOSDS(final String id, final String name, final String sdsState, final int port)
    {
        this.id = id;
        this.name = name;
        this.sdsState = sdsState;
        this.port = port;
    }

    //    public String getUuid()
    //    {
    //        return uuid;
    //    }
    //
    //    public void setUuid(final String uuid)
    //    {
    //        this.uuid = uuid;
    //    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getSdsState()
    {
        return sdsState;
    }

    public void setSdsState(final String sdsState)
    {
        this.sdsState = sdsState;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(final int port)
    {
        this.port = port;
    }

    public ScaleIOData getScaleIOData()
    {
        return scaleIOData;
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public ScaleIOFaultSet getFaultSet()
    {
        return faultSet;
    }

    public void setFaultSet(final ScaleIOFaultSet faultSet)
    {
        this.faultSet = faultSet;
    }

    public ScaleIOProtectionDomain getProtectionDomain()
    {
        return protectionDomain;
    }

    public void setProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomain = protectionDomain;
    }

    public void addRoleIP(final ScaleIORoleIP roleIP1)
    {
        this.roleIPs.add(roleIP1);
    }

    public List<ScaleIORoleIP> getRoleIPs()
    {
        return roleIPs;
    }

    public void addDevice(final ScaleIODevice device)
    {
        this.devices.add(device);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(sdsState).append(roleIPs).append(devices).toHashCode();
    }

    /**
     * For the sake of non-circular checks "equals" checks for relationship attributes must be checked
     * on only one side of the relationship. In the case of OneToMany relationships it will be done on
     * the "One" side (the one holding the List)
     * <p>
     * On the "Many" Side we'll ignore the attribute when doing the equals comparison as a way to avoid
     * a circular reference starting and endless cycle.
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
        if (!(other instanceof ScaleIOSDS))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOSDS toot = ((ScaleIOSDS) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name).append(sdsState, toot.sdsState)
                .append(roleIPs, toot.roleIPs).append(devices, toot.devices).isEquals();
    }
}
