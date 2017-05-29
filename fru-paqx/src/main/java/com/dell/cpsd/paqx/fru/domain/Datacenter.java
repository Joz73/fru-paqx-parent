/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DATACENTER")
public class Datacenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DATACENTER_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "DATACENTER_NAME")
    private String name;

    public Datacenter(){}

    public Datacenter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datacenter", orphanRemoval = true)
    private List<DVSwitch> dvSwitchList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datacenter", orphanRemoval = true)
    private List<Datastore> datastoreList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datacenter", orphanRemoval = true)
    private List<Cluster> clusterList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private VCenter vCenter;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DVSwitch> getDvSwitchList() {
        return dvSwitchList;
    }

    public void addDvSwitch(DVSwitch dvSwitch) {
        this.dvSwitchList.add(dvSwitch);
    }

    public void setDvSwitchList(List<DVSwitch> dvSwitchList) {
        this.dvSwitchList = dvSwitchList;
    }

    public List<Datastore> getDatastoreList() {
        return datastoreList;
    }

    public void addDatastore(Datastore datastore) {
        this.datastoreList.add(datastore);
    }

    public void setDatastoreList(List<Datastore> datastoreList) {
        this.datastoreList = datastoreList;
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public void addCluster(Cluster cluster) {
        this.clusterList.add(cluster);
    }

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    public VCenter getvCenter() {
        return vCenter;
    }

    public void setvCenter(VCenter vCenter) {
        this.vCenter = vCenter;
    }
}
