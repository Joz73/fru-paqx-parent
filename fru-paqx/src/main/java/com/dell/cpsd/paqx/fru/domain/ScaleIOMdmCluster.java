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
@Table(name = "SCALEIO_MDM_CLUSTER")
public class ScaleIOMdmCluster
{
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "MDM_CLUSTER_UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "MDM_CLUSTER_ID", unique = true, nullable = false)
    private String id;

    @Column(name = "MDM_CLUSTER_NAME")
    private String name;

    @Column(name = "MDM_CLUSTER_STATE")
    private String clusterState;

    @Column(name = "MDM_CLUSTER_MODE")
    private String clusterMode;

    @Column(name = "MDM_CLUSTER_GOOD_NODES_NUM")
    private Integer goodNodesNum;

    @Column(name = "MDM_CLUSTER_GOOD_REPLICAS_NUM")
    private Integer goodReplicasNum;

    @OneToOne(optional = false, mappedBy = "mdmCluster", cascade = CascadeType.ALL)
    private ScaleIOData scaleIOData;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> slaveElementInfo = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> tiebreakerElementInfo = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mdmCluster", orphanRemoval = true)
    List<ScaleIOSDSElementInfo> masterElementInfo = new ArrayList<>();

    public ScaleIOMdmCluster()
    {
    }

    public ScaleIOMdmCluster(final String id8, final String clusterName, final String clusterState, final String clusterMode, final int i,
            final int i1)
    {
        this.id = id8;
        this.name = clusterName;
        this.clusterState = clusterState;
        this.clusterMode = clusterMode;
        this.goodNodesNum = i;
        this.goodReplicasNum = i1;
    }

    public Long getUuid()
    {
        return uuid;
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

    public String getClusterState()
    {
        return clusterState;
    }

    public void setClusterState(final String clusterState)
    {
        this.clusterState = clusterState;
    }

    public String getClusterMode()
    {
        return clusterMode;
    }

    public void setClusterMode(final String clusterMode)
    {
        this.clusterMode = clusterMode;
    }

    public Integer getGoodNodesNum()
    {
        return goodNodesNum;
    }

    public void setGoodNodesNum(final Integer goodNodesNum)
    {
        this.goodNodesNum = goodNodesNum;
    }

    public Integer getGoodReplicasNum()
    {
        return goodReplicasNum;
    }

    public void setGoodReplicasNum(final Integer goodReplicasNum)
    {
        this.goodReplicasNum = goodReplicasNum;
    }

    public void setScaleIOData(final ScaleIOData scaleIOData)
    {
        this.scaleIOData = scaleIOData;
    }

    public ScaleIOData getScaleIOData()
    {
        return scaleIOData;
    }

    public void addSlave(final ScaleIOSlaveElementInfo slave)
    {
        this.slaveElementInfo.add(slave);
    }

    public void addMaster(final ScaleIOMasterElementInfo slave)
    {
        this.masterElementInfo.add(slave);
    }

    public void addTiebreaker(final ScaleIOTiebreakerElementInfo slave)
    {
        this.tiebreakerElementInfo.add(slave);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uuid).append(id).append(name).append(clusterState).append(clusterMode).append(goodNodesNum)
                .append(goodReplicasNum).append(scaleIOData).append(slaveElementInfo).append(tiebreakerElementInfo)
                .append(masterElementInfo).toHashCode();
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
     * shall be made on this side.
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
        if (!(other instanceof ScaleIOMdmCluster))
        {
            return false;
        }
        //Toot stands for "That Object Over There"
        ScaleIOMdmCluster toot = ((ScaleIOMdmCluster) other);
        return new EqualsBuilder().append(uuid, toot.uuid).append(id, toot.id).append(name, toot.name)
                .append(clusterState, toot.clusterState).append(clusterMode, toot.clusterMode).append(goodNodesNum, toot.goodNodesNum)
                .append(goodReplicasNum, toot.goodReplicasNum).append(scaleIOData, toot.scaleIOData)
                .append(slaveElementInfo, toot.slaveElementInfo).append(tiebreakerElementInfo, toot.tiebreakerElementInfo)
                .append(masterElementInfo, toot.masterElementInfo).isEquals();
    }
}
