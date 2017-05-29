/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PHYSICAL_NIC")
public class PhysicalNic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DEVICE")
    private String device;

    @Column(name = "DRIVER")
    private String driver;

    @Column(name = "MAC")
    private String mac;

    @Column(name = "PCI")
    private String pci;

    @ManyToOne(cascade = CascadeType.ALL)
    private Host host;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "physicalNic", orphanRemoval = true)
    List<PhysicalNicDVSConnection> physicalNicDVSConnectionList = new ArrayList<>();

    public PhysicalNic() {
    }

    public PhysicalNic(String device, String driver, String mac, String pci) {
        this.device = device;
        this.driver = driver;
        this.mac = mac;
        this.pci = pci;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPci() {
        return pci;
    }

    public void setPci(String pci) {
        this.pci = pci;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<PhysicalNicDVSConnection> getPhysicalNicDVSConnectionList() {
        return physicalNicDVSConnectionList;
    }

    public void setPhysicalNicDVSConnectionList(List<PhysicalNicDVSConnection> physicalNicDVSConnectionList) {
        this.physicalNicDVSConnectionList = physicalNicDVSConnectionList;
    }
}
