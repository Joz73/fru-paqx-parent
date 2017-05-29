/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "PHYSICAL_NIC_DVS_CONNECTION")
public class PhysicalNicDVSConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "UPLINK_PORT_KEY")
    private String uplinkPortKey;

    @ManyToOne
    private PhysicalNic physicalNic;

    @ManyToOne
    private DVSwitch dvSwitch;

    public PhysicalNicDVSConnection() {
    }

    public PhysicalNicDVSConnection(String uplinkPortKey) {
        this.uplinkPortKey = uplinkPortKey;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getUplinkPortKey() {
        return uplinkPortKey;
    }

    public void setUplinkPortKey(String uplinkPortKey) {
        this.uplinkPortKey = uplinkPortKey;
    }

    public PhysicalNic getPhysicalNic() {
        return physicalNic;
    }

    public void setPhysicalNic(PhysicalNic physicalNic) {
        this.physicalNic = physicalNic;
    }

    public DVSwitch getDvSwitch() {
        return dvSwitch;
    }

    public void setDvSwitch(DVSwitch dvSwitch) {
        this.dvSwitch = dvSwitch;
    }
}
