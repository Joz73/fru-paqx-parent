/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DVSWITCH")
public class DVSwitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DVSWITCH_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "DVSWITCH_NAME")
    private String name;

    @Column(name = "ALLOW_PROMISCUOUS")
    private boolean allowPromiscuous;

    public DVSwitch(){}

    public DVSwitch(String id, String name, boolean allowPromiscuous) {
        this.id = id;
        this.name = name;
        this.allowPromiscuous = allowPromiscuous;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Datacenter datacenter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dvSwitch", orphanRemoval = true)
    List<PhysicalNicDVSConnection> physicalNicDVSConnectionList = new ArrayList<>();

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

    public boolean isAllowPromiscuous() {
        return allowPromiscuous;
    }

    public void setAllowPromiscuous(boolean allowPromiscuous) {
        this.allowPromiscuous = allowPromiscuous;
    }

    public Datacenter getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

    public List<PhysicalNicDVSConnection> getPhysicalNicDVSConnectionList() {
        return physicalNicDVSConnectionList;
    }

    public void setPhysicalNicDVSConnectionList(List<PhysicalNicDVSConnection> physicalNicDVSConnectionList) {
        this.physicalNicDVSConnectionList = physicalNicDVSConnectionList;
    }
}
