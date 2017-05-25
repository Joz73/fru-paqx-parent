/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.amqp.config;

import com.dell.cpsd.paqx.fru.rest.repository.DataServiceRepository;
import com.dell.cpsd.paqx.fru.rest.repository.H2DataServiceRepository;
import com.dell.cpsd.paqx.fru.service.DataService;
import com.dell.cpsd.paqx.fru.service.DataServiceImpl;
import com.dell.cpsd.paqx.fru.transformers.DestroyVMDtoToDestroyVMRequestMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterDomainTransformer;
import com.dell.cpsd.paqx.fru.transformers.DiscoveryInfoToVCenterSystemPropertiesTransformer;
import com.dell.cpsd.paqx.fru.transformers.HostListToHostRepresentationTransformer;
import com.dell.cpsd.paqx.fru.transformers.SDSListDtoToRemoveScaleIOMessageTransformer;
import com.dell.cpsd.paqx.fru.transformers.ScaleIORestToScaleIODomainTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Configuration
@Profile("UnitTest")
public class TestConfig
{
    @Bean
    @Qualifier("test")
    public DataServiceRepository dataServiceRepository()
    {
        return new H2DataServiceRepository();
    }

    @Bean
    @Qualifier("testService")
    DataService dataService()
    {
        return new DataServiceImpl(dataServiceRepository(), new ScaleIORestToScaleIODomainTransformer(),
                new DiscoveryInfoToVCenterDomainTransformer(), new HostListToHostRepresentationTransformer(),
                new SDSListDtoToRemoveScaleIOMessageTransformer(), new DestroyVMDtoToDestroyVMRequestMessageTransformer());
    }
}