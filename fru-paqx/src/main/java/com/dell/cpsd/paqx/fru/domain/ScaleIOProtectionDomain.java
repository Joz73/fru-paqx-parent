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
@Table(name = "SCALE_IO_PROTETION_DOMAIN")
public class ScaleIOProtectionDomain
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "PROTECTION_DOMAIN_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "PROTECTION_DOMAIN_ID")
    private String id;

    @Column(name = "PROTECTION_DOMAIN_NAME")
    private String name;

    @Column(name = "PROTECTION_DOMAIN_STATE")
    private String protectionDomainState;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOFaultSet> faultSets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOSDS> sdsList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protectionDomain", orphanRemoval = true)
    private List<ScaleIOStoragePool> storagePools = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;

    public ScaleIOProtectionDomain()
    {

    }

    public ScaleIOProtectionDomain(final String id, final String protectionDomainName, final String state)
    {
        this.id = id;
        this.name = protectionDomainName;
        this.protectionDomainState = state;
    }

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

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

    public String getProtectionDomainState()
    {
        return protectionDomainState;
    }

    public void setProtectionDomainState(final String protectionDomainState)
    {
        this.protectionDomainState = protectionDomainState;
    }

    public void addFaultSet(final ScaleIOFaultSet faultSet)
    {
        this.faultSets.add(faultSet);
    }

    public void addSDS(final ScaleIOSDS sds)
    {
        this.sdsList.add(sds);
    }

    public ScaleIOData getScaleIOData()
    {
        return scaleIOData;
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public void addStoragePool(final ScaleIOStoragePool storagePool)
    {
        storagePools.add(storagePool);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(protectionDomainState).append(faultSets).append(sdsList)
                .append(storagePools).toHashCode();
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
        if (!(other instanceof ScaleIOProtectionDomain))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOProtectionDomain toot = ((ScaleIOProtectionDomain) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name)
                .append(protectionDomainState, toot.protectionDomainState).append(faultSets, toot.faultSets).append(sdsList, toot.sdsList)
                .append(storagePools, toot.storagePools).isEquals();
    }
}
