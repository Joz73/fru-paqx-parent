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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ELEMENT_TYPE")
@Table(name = "SCALEIO_SDS_ELEMENT_INFO")
public abstract class ScaleIOSDSElementInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SDS_ELEMENT_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SDS_ELEMENT_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "SDS_ELEMENT_PORT")
    private Integer port;

    @Column(name = "SDS_ELEMENT_VERSION_INFO")
    private String versionInfo;

    @Column(name = "SDS_ELEMENT_NAME")
    private String name;

    @Column(name = "SDS_ELEMENT_ROLE")
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sdsElementInfo", orphanRemoval = true)
    private List<ScaleIOIP> managementIPs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sdsElementInfo", orphanRemoval = true)
    private List<ScaleIOIP> ips = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    ScaleIOMdmCluster mdmCluster;

    public ScaleIOSDSElementInfo(final String id, final int port, final String versionInfo, final String name, final String role)
    {
        this.id = id;
        this.port = port;
        this.versionInfo = versionInfo;
        this.name = name;
        this.role = role;
    }

    public ScaleIOSDSElementInfo()
    {

    }

    public Long getUuid()
    {
        return uuid;
    }

    public void setUuid(final Long uuid)
    {
        this.uuid = uuid;
    }

    public ScaleIOMdmCluster getMdmCluster()
    {
        return mdmCluster;
    }

    public void setMdmCluster(final ScaleIOMdmCluster mdmCluster)
    {
        this.mdmCluster = mdmCluster;
    }

    public void addIP(final ScaleIOIP ip)
    {
        this.ips.add(ip);
    }

    public void addManagementIP(final ScaleIOIP ip)
    {
        this.managementIPs.add(ip);
    }

    public String getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(port).append(versionInfo).append(name).append(role)
                .append(managementIPs).append(ips).toHashCode();
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
        if (!(other instanceof ScaleIOSDSElementInfo))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOSDSElementInfo toot = ((ScaleIOSDSElementInfo) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(port, toot.port).append(versionInfo, toot.versionInfo)
                .append(name, toot.name).append(role, toot.role).append(managementIPs, toot.managementIPs).append(ips, toot.ips).isEquals();
    }
}
