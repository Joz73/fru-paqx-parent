/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VCENTER")
public class VCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "VCENTER_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "VCENTER_NAME")
    private String name;

    public VCenter()
    {

    }

    public VCenter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vCenter", orphanRemoval = true)
    private List<Datacenter> datacenterList = new ArrayList<>();

    @OneToOne(optional = false, mappedBy = "vcenter", cascade = CascadeType.ALL)
    private FruJob job;

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

    public List<Datacenter> getDatacenterList() {
        return datacenterList;
    }

    public void setDatacenterList(List<Datacenter> datacenterList) {
        this.datacenterList = datacenterList;
    }

    public void addDatacenter(Datacenter datacenter){
        this.datacenterList.add(datacenter);
    }
}
