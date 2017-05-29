/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;

@Entity
@Table(name = "VIRTUAL_NIC_DV_PORTGROUP")
public class VirtualNicDVPortGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "PORT_KEY")
    private String port;

    @OneToOne
    private VirtualNic virtualNic;

    @ManyToOne
    private DVPortGroup dvPortGroup;
}
