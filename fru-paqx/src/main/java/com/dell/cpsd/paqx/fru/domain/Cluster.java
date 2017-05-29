/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLUSTER")
public class Cluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "CLUSTER_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "CLUSTER_NAME")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Datacenter datacenter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cluster", orphanRemoval = true)
    List<Host> hostList = new ArrayList<>();

    public Cluster(){}

    public Cluster(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Datacenter getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

    public List<Host> getHostList() {
        return hostList;
    }

    public void setHostList(List<Host> hostList) {
        this.hostList = hostList;
    }

    public void addHost(Host host) {
        this.hostList.add(host);
    }
}
