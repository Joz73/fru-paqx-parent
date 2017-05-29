/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "HOST_IP_ROUTE_CONFIG")
public class HostIpRouteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DEFAULT_GATEWAY")
    private String defaultGateway;

    @Column(name = "DEFAULT_GATEWAY_DEVICE")
    private String defaultGatewayDevice;

    @Column(name = "IPV6_DEFAULT_GATEWAY")
    private String ipV6defaultGateway;

    @Column(name = "IPV6_DEFAULT_GATEWAY_DEVICE")
    private String ipV6defaultGatewayDevice;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="HOST_ID")
    private Host host;

    public HostIpRouteConfig() {
    }

    public HostIpRouteConfig(String defaultGateway, String defaultGatewayDevice, String ipV6defaultGateway, String ipV6defaultGatewayDevice) {
        this.defaultGateway = defaultGateway;
        this.defaultGatewayDevice = defaultGatewayDevice;
        this.ipV6defaultGateway = ipV6defaultGateway;
        this.ipV6defaultGatewayDevice = ipV6defaultGatewayDevice;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    public String getDefaultGatewayDevice() {
        return defaultGatewayDevice;
    }

    public void setDefaultGatewayDevice(String defaultGatewayDevice) {
        this.defaultGatewayDevice = defaultGatewayDevice;
    }

    public String getIpV6defaultGateway() {
        return ipV6defaultGateway;
    }

    public void setIpV6defaultGateway(String ipV6defaultGateway) {
        this.ipV6defaultGateway = ipV6defaultGateway;
    }

    public String getIpV6defaultGatewayDevice() {
        return ipV6defaultGatewayDevice;
    }

    public void setIpV6defaultGatewayDevice(String ipV6defaultGatewayDevice) {
        this.ipV6defaultGatewayDevice = ipV6defaultGatewayDevice;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}

