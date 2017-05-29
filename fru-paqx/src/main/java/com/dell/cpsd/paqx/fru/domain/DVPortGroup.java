/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DV_PORTGROUP")
public class DVPortGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DV_PORTGROUP_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "dvPortGroupList")
    private List<VMNetwork> vmNetworkList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dvPortGroup")
    private List<VirtualNicDVPortGroup> virtualNicDVPortGroups;

    public DVPortGroup() {
    }

    public DVPortGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<VMNetwork> getVmNetworkList() {
        return vmNetworkList;
    }

    public void setVmNetworkList(List<VMNetwork> vmNetworkList) {
        this.vmNetworkList = vmNetworkList;
    }

    public List<VirtualNicDVPortGroup> getVirtualNicDVPortGroups() {
        return virtualNicDVPortGroups;
    }

    public void setVirtualNicDVPortGroups(List<VirtualNicDVPortGroup> virtualNicDVPortGroups) {
        this.virtualNicDVPortGroups = virtualNicDVPortGroups;
    }
}
