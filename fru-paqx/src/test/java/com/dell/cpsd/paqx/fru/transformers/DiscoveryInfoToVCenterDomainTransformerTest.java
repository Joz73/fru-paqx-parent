/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.domain.VCenter;
import com.dell.cpsd.virtualization.capabilities.api.Datacenter;
import com.dell.cpsd.virtualization.capabilities.api.DiscoveryResponseInfoMessage;
import org.junit.Test;

import java.util.Arrays;


import static org.junit.Assert.*;

public class DiscoveryInfoToVCenterDomainTransformerTest {
    @Test
    public void testNullCase()
    {
        DiscoveryInfoToVCenterDomainTransformer transformer = new DiscoveryInfoToVCenterDomainTransformer();
        assertNull(transformer.transform(null));
    }

    @Test
    public void testEmptyResp()
    {
        DiscoveryInfoToVCenterDomainTransformer transformer = new DiscoveryInfoToVCenterDomainTransformer();
        DiscoveryResponseInfoMessage resp = new DiscoveryResponseInfoMessage();
        assertTrue(transformer.transform(resp) != null);
    }

    @Test
    public void testEmptyDatacenter()
    {
        DiscoveryInfoToVCenterDomainTransformer transformer = new DiscoveryInfoToVCenterDomainTransformer();
        DiscoveryResponseInfoMessage resp = new DiscoveryResponseInfoMessage();
        resp.setDatacenters(Arrays.asList(new Datacenter()));
        VCenter data = transformer.transform(resp);
        assertNotNull(data);
    }

}
