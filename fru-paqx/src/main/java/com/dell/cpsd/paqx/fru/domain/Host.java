/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HOST")
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "HOST_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "HOST_NAME")
    private String name;

    @Column(name = "POWER_STATE")
    private String powerState;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cluster cluster;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    List<VSwitch> vSwitchList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true, fetch = FetchType.EAGER)
    List<VirtualNic> virtualNicList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    List<PhysicalNic> physicalNicList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    List<VirtualMachine> virtualMachineList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    private HostIpRouteConfig hostIpRouteConfig;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "host", orphanRemoval = true)
    private HostDnsConfig hostDnsConfig;

    public Host(){}

    public Host(String id, String name, String powerState) {
        this.id = id;
        this.name = name;
        this.powerState = powerState;
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

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public List<VSwitch> getvSwitchList() {
        return vSwitchList;
    }

    public void addVSwitch(VSwitch vSwitch) {
        this.vSwitchList.add(vSwitch);
    }

    public void setvSwitchList(List<VSwitch> vSwitchList) {
        this.vSwitchList = vSwitchList;
    }

    public List<VirtualNic> getVirtualNicList() {
        return virtualNicList;
    }

    public void addVirtualNic(VirtualNic virtualNic) {
        this.virtualNicList.add(virtualNic);
    }

    public void setVirtualNicList(List<VirtualNic> virtualNicList) {
        this.virtualNicList = virtualNicList;
    }

    public HostIpRouteConfig getHostIpRouteConfig() {
        return hostIpRouteConfig;
    }

    public void setHostIpRouteConfig(HostIpRouteConfig hostIpRouteConfig) {
        this.hostIpRouteConfig = hostIpRouteConfig;
    }

    public HostDnsConfig getHostDnsConfig() {
        return hostDnsConfig;
    }

    public void setHostDnsConfig(HostDnsConfig hostDnsConfig) {
        this.hostDnsConfig = hostDnsConfig;
    }

    public List<PhysicalNic> getPhysicalNicList() {
        return physicalNicList;
    }

    public void setPhysicalNicList(List<PhysicalNic> physicalNicList) {
        this.physicalNicList = physicalNicList;
    }

    public List<VirtualMachine> getVirtualMachineList() {
        return virtualMachineList;
    }

    public void setVirtualMachineList(List<VirtualMachine> virtualMachineList) {
        this.virtualMachineList = virtualMachineList;
    }
}
