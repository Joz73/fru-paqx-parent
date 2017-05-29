/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VM_GUEST_NETWORK")
public class VMNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "CONNECTED")
    private boolean connected;

    @Column(name = "MAC_ADDRESS")
    private String macAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    private VirtualMachine virtualMachine;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vmNetwork", orphanRemoval = true)
    private List<VMIP> vmip = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "VM_NETWORK_DVPORTGROUP", joinColumns = {
            @JoinColumn(name = "VM_NETWORK_ID", nullable = false)},
            inverseJoinColumns = { @JoinColumn(name = "DV_PORTGROUP_ID", nullable = false)})
    private List<DVPortGroup> dvPortGroupList = new ArrayList<>();

    public VMNetwork(){}

    public VMNetwork(boolean connected, String macAddress) {
        this.connected = connected;
        this.macAddress = macAddress;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public List<VMIP> getVmip() {
        return vmip;
    }

    public void setVmip(List<VMIP> vmip) {
        this.vmip = vmip;
    }

    public List<DVPortGroup> getDvPortGroupList() {
        return dvPortGroupList;
    }

    public void setDvPortGroupList(List<DVPortGroup> dvPortGroupList) {
        this.dvPortGroupList = dvPortGroupList;
    }
}
