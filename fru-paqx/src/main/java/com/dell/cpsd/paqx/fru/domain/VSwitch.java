/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "VSWITCH")
public class VSwitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "VSWITCH_ID", nullable=false)
    private String id;

    @Column(name = "VSWITCH_NAME")
    private String name;

    @Column(name = "ALLOW_PROMISCUOUS")
    private boolean allowPromiscuous;

    @ManyToOne(cascade = CascadeType.ALL)
    private Host host;

    public VSwitch(){}

    public VSwitch(String id, String name, boolean allowPromiscuous) {
        this.id = id;
        this.name = name;
        this.allowPromiscuous = allowPromiscuous;
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

    public boolean isAllowPromiscuous() {
        return allowPromiscuous;
    }

    public void setAllowPromiscuous(boolean allowPromiscuous) {
        this.allowPromiscuous = allowPromiscuous;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
