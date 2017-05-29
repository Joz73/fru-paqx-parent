/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "HOST_DNS_CONFIG")
public class HostDnsConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID", unique = true, nullable = false)
    private Long uuid;

    @Column(name = "DHCP")
    private boolean dhcp;

    @Column(name = "DOMAIN_NAME")
    private String domainName;

    @Column(name = "HOSTNAME")
    private String hostname;

    @ElementCollection
    @CollectionTable(name = "SEARCH_DOMAIN", joinColumns = @JoinColumn(name = "UUID"))
    private List<String> searchDomains;

    @ElementCollection
    @CollectionTable(name = "DNS_CONFIG_IP", joinColumns = @JoinColumn(name = "UUID"))
    private List<String> dnsConfigIPs;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="HOST_ID")
    private Host host;

    public HostDnsConfig() {
    }

    public HostDnsConfig(boolean dhcp, String domainName, String hostname, List<String> searchDomains, List<String> dnsConfigIPs) {
        this.dhcp = dhcp;
        this.domainName = domainName;
        this.hostname = hostname;
        this.searchDomains = searchDomains;
        this.dnsConfigIPs = dnsConfigIPs;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public boolean isDhcp() {
        return dhcp;
    }

    public void setDhcp(boolean dhcp) {
        this.dhcp = dhcp;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getSearchDomains() {
        return searchDomains;
    }

    public void setSearchDomains(List<String> searchDomains) {
        this.searchDomains = searchDomains;
    }

    public List<String> getDnsConfigIPs() {
        return dnsConfigIPs;
    }

    public void setDnsConfigIPs(List<String> dnsConfigIPs) {
        this.dnsConfigIPs = dnsConfigIPs;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
