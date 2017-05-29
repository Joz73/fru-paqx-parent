/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "VIRTUAL_NIC")
public class VirtualNic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "VNIC_DEVICE")
    private String device;

    @Column(name = "VNIC_PORT")
    private String port;

    @Column(name = "VNIC_PORTGROUP")
    private String portGroup;

    @Column(name = "VNIC_MAC")
    private String mac;

    @Column(name = "DHCP")
    private boolean dhcp;

    @Column(name = "VNIC_IP")
    private String ip;

    @Column(name = "VNIC_SUBNET_MASK")
    private String subnetMask;

    @ManyToOne(cascade = CascadeType.ALL)
    private Host host;

    @OneToOne(mappedBy = "virtualNic")
    private VirtualNicDVPortGroup virtualNicDVPortGroup;

    public VirtualNic(){}

    public VirtualNic(String device, String port, String portGroup, String mac, boolean dhcp, String ip, String subnetMask) {
        this.device = device;
        this.port = port;
        this.portGroup = portGroup;
        this.mac = mac;
        this.dhcp = dhcp;
        this.ip = ip;
        this.subnetMask = subnetMask;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPortGroup() {
        return portGroup;
    }

    public void setPortGroup(String portGroup) {
        this.portGroup = portGroup;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isDhcp() {
        return dhcp;
    }

    public void setDhcp(boolean dhcp) {
        this.dhcp = dhcp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
