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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Entity
@Table(name = "SCALEIO_DATA")
public class ScaleIOData
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SCALEIO_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "SCALEIO_ID", unique = true)
    private String id;

    @Column(name = "SCALEIO_NAME")
    private String name;

    @Column(name = "SCALEIO_INSTALLID")
    private String installId;

    @Column(name = "SCALEIO_MDM_MODE")
    private String mdmMode;

    @Column(name = "SCALEIO_SYSTEM_VERSION_NAME")
    private String systemVersionName;

    @Column(name = "SCALEIO_MDM_CLUSTER_STATE")
    private String mdmClusterState;

    @Column(name = "SCALEIO_VERSION")
    private String version;

    @OneToOne(optional = false, mappedBy = "scaleIOData", cascade = CascadeType.ALL)
    private FruJob job;

    @OneToOne(cascade = CascadeType.ALL)
    private ScaleIOMdmCluster mdmCluster = null;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOSDC> sdcList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOSDS> sdsList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> tiebreakerScaleIOList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> primaryMDMIPList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOIP> secondaryMDMIPList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scaleIOData", orphanRemoval = true)
    List<ScaleIOProtectionDomain> protectionDomains = new ArrayList<>();

    public ScaleIOData()
    {

    }

    public ScaleIOData(final String id1, final String scaleIODataName, final String scaleIODataInstallID, final String mdmMode,
            final String systemVersionName, final String mdmClusterState, final String version)
    {
        this.id = id1;
        this.name = scaleIODataName;
        this.installId = scaleIODataInstallID;
        this.mdmMode = mdmMode;
        this.systemVersionName = systemVersionName;
        this.mdmClusterState = mdmClusterState;
        this.version = version;
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

    public String getInstallId()
    {
        return installId;
    }

    public void setInstallId(final String installId)
    {
        this.installId = installId;
    }

    public String getMdmMode()
    {
        return mdmMode;
    }

    public void setMdmMode(final String mdmMode)
    {
        this.mdmMode = mdmMode;
    }

    public String getSystemVersionName()
    {
        return systemVersionName;
    }

    public void setSystemVersionName(final String systemVersionName)
    {
        this.systemVersionName = systemVersionName;
    }

    public String getMdmClusterState()
    {
        return mdmClusterState;
    }

    public void setMdmClusterState(final String mdmClusterState)
    {
        this.mdmClusterState = mdmClusterState;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(final String version)
    {
        this.version = version;
    }

    public ScaleIOMdmCluster getMdmCluster()
    {
        return mdmCluster;
    }

    public void setMdmCluster(final ScaleIOMdmCluster mdmCluster)
    {
        this.mdmCluster = mdmCluster;
    }

    public List<ScaleIOSDC> getSdcList()
    {
        return sdcList;
    }

    public void addSdc(final ScaleIOSDC sdc)
    {
        this.sdcList.add(sdc);
    }

    public List<ScaleIOSDS> getSdsList()
    {
        return sdsList;
    }

    public void addSds(final ScaleIOSDS sds)
    {
        this.sdsList.add(sds);
    }

    public List<ScaleIOIP> getTiebreakerScaleIOList()
    {
        return tiebreakerScaleIOList;
    }

    public void setTiebreakerScaleIOList(final List<ScaleIOIP> tiebreakerScaleIOList)
    {
        this.tiebreakerScaleIOList = tiebreakerScaleIOList;
    }

    public List<ScaleIOIP> getPrimaryMDMIPList()
    {
        return primaryMDMIPList;
    }

    public void setPrimaryMDMIPList(final List<ScaleIOIP> primaryMDMIPList)
    {
        this.primaryMDMIPList = primaryMDMIPList;
    }

    public List<ScaleIOIP> getSecondaryMDMIPList()
    {
        return secondaryMDMIPList;
    }

    public void setSecondaryMDMIPList(final List<ScaleIOIP> secondaryMDMIPList)
    {
        this.secondaryMDMIPList = secondaryMDMIPList;
    }

    public void addProtectionDomain(final ScaleIOProtectionDomain protectionDomain)
    {
        this.protectionDomains.add(protectionDomain);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(installId).append(mdmMode).append(systemVersionName)
                .append(mdmClusterState).append(version).append(sdcList).append(sdsList).append(tiebreakerScaleIOList)
                .append(primaryMDMIPList).append(secondaryMDMIPList).append(protectionDomains).toHashCode();
    }

    /**
     * For the sake of non-circular checks "equals" checks for relationship attributes must be checked
     * on only one side of the relationship. In the case of OneToMany relationships it will be done on
     * the "One" side (the one holding the List)
     * <p>
     * On the "Many" Side we'll ignore the attribute when doing the equals comparison as a way to avoid
     * a circular reference starting and endless cycle.
     * <p>
     * In the case of OneToOne relationships, one of the sides have to be chosen. For the case of Clusters
     * and Systems the representative side chosen will be the MDM Cluster Side, therefore the equals comparison
     * shall be made on that side and we will ignore the attribute on this side.
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
        if (!(other instanceof ScaleIOData))
        {
            return false;
        }

        //Toot stands for "That Object Over There"
        ScaleIOData toot = ((ScaleIOData) other);

        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name).append(installId, toot.installId)
                .append(mdmMode, toot.mdmMode).append(systemVersionName, toot.systemVersionName)
                .append(mdmClusterState, toot.mdmClusterState).append(version, toot.version).append(sdcList, toot.sdcList)
                .append(sdsList, toot.sdsList).append(tiebreakerScaleIOList, toot.tiebreakerScaleIOList)
                .append(primaryMDMIPList, toot.primaryMDMIPList).append(secondaryMDMIPList, toot.secondaryMDMIPList)
                .append(protectionDomains, toot.protectionDomains).isEquals();
    }
}
