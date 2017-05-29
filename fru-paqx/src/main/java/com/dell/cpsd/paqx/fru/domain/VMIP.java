/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "VM_IP")
public class VMIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    private VMNetwork vmNetwork;

    public VMIP(){}

    public VMIP(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public VMNetwork getVmNetwork() {
        return vmNetwork;
    }

    public void setVmNetwork(VMNetwork vmNetwork) {
        this.vmNetwork = vmNetwork;
    }
}
