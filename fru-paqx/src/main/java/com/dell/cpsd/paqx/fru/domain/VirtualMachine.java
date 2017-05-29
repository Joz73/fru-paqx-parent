/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VIRTUAL_MACHINE")
public class VirtualMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "VM_ID", unique=true, nullable=false)
    private String id;

    @Column(name = "VM_NAME")
    private String name;

    @Column(name = "VM_POWERSTATE")
    private String powerState;

    @Column(name = "VM_GUEST_HOSTNAME")
    private String guestHostname;

    @Column(name = "VM_GUEST_OS")
    private String guestOS;

    @ManyToOne(cascade = CascadeType.ALL)
    private Host host;

    @ManyToOne(cascade = CascadeType.ALL)
    private Datastore datastore;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "virtualMachine", orphanRemoval = true)
    private List<VMNetwork> vmNetworkList = new ArrayList<>();

    public VirtualMachine(){}

    public VirtualMachine(String id, String name, String powerState, String guestHostname, String guestOS) {
        this.id = id;
        this.name = name;
        this.powerState = powerState;
        this.guestHostname = guestHostname;
        this.guestOS = guestOS;
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

    public String getGuestHostname() {
        return guestHostname;
    }

    public void setGuestHostname(String guestHostname) {
        this.guestHostname = guestHostname;
    }

    public String getGuestOS() {
        return guestOS;
    }

    public void setGuestOS(String guestOS) {
        this.guestOS = guestOS;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    public List<VMNetwork> getVmNetworkList() {
        return vmNetworkList;
    }

    public void setVmNetworkList(List<VMNetwork> vmNetworkList) {
        this.vmNetworkList = vmNetworkList;
    }

    public void addVmNetwork(VMNetwork vmNetwork){
        this.vmNetworkList.add(vmNetwork);
    }
}
