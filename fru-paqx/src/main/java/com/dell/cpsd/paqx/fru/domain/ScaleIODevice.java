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
@Table(name="SDC_DEVICE")
public class ScaleIODevice
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "DEVICE_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DEVICE_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "DEVICE_NAME")
    private String name;

    @Column(name = "DEVICE_CURRENT_PATH_NAME")
    private String deviceCurrentPathName;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOStoragePool storagePool;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScaleIOSDS sds;

    public ScaleIODevice()
    {

    }

    public ScaleIODevice(final String id, final String scaleIOName, final String deviceCurrentPathName)
    {
        this.id = id;
        this.name = scaleIOName;
        this.deviceCurrentPathName = deviceCurrentPathName;
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

    public String getDeviceCurrentPathName()
    {
        return deviceCurrentPathName;
    }

    public void setDeviceCurrentPathName(final String deviceCurrentPathName)
    {
        this.deviceCurrentPathName = deviceCurrentPathName;
    }

    public void setStoragePool(final ScaleIOStoragePool storagePool)
    {
        this.storagePool = storagePool;
    }

    public void setSds(final ScaleIOSDS sds)
    {
        this.sds = sds;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(deviceCurrentPathName).toHashCode();
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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ScaleIODevice)) {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIODevice toot = ((ScaleIODevice) other);

        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name)
                .append(deviceCurrentPathName, toot.deviceCurrentPathName).isEquals();
    }
}
