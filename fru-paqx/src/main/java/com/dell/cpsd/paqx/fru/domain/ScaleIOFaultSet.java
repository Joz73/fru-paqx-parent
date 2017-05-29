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
@Table(name = "SCALEIO_FAULTSET")
public class ScaleIOFaultSet
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "FAULTSET_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "FAULTSET_ID")
    private String id;

    @Column(name = "FAULTSET_NAME")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOProtectionDomain protectionDomain;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faultSet", orphanRemoval = true)
    private List<ScaleIOSDS> sdsList = new ArrayList<>();

    public ScaleIOFaultSet()
    {

    }

    public ScaleIOFaultSet(final String id, final String faultSetName)
    {
        this.id = id;
        this.name = faultSetName;
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

    public void setProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomain = protectionDomain;
    }

    public void addSDS(final ScaleIOSDS sds)
    {
        this.sdsList.add(sds);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(sdsList).toHashCode();
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
        if (!(other instanceof ScaleIOFaultSet))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOFaultSet toot = ((ScaleIOFaultSet) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name).append(sdsList, toot.sdsList)
                .isEquals();
    }
}
