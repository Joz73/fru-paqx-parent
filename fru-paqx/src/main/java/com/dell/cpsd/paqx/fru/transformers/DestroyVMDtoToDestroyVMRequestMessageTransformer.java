/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.dto.DestroyVMDto;
import com.dell.cpsd.virtualization.capabilities.api.Credentials;
import com.dell.cpsd.virtualization.capabilities.api.DestroyVMRequestMessage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class DestroyVMDtoToDestroyVMRequestMessageTransformer
{
    public List<DestroyVMRequestMessage> transform(final List<DestroyVMDto> destroyVMDtos)
    {
        if (destroyVMDtos==null)
        {
            return null;
        }

        return destroyVMDtos.stream().filter(Objects::nonNull).map(x->transform(x)).collect(Collectors.toList());
    }

    private DestroyVMRequestMessage transform(final DestroyVMDto destroyVMDto)
    {
        if (destroyVMDto==null)
        {
            return null;
        }

        DestroyVMRequestMessage returnVal = new DestroyVMRequestMessage();
        returnVal.setCredentials(transformToCredentials(destroyVMDto.getUsername(), destroyVMDto.getPassword(), destroyVMDto.getEndpoint()));
        returnVal.setUuid(destroyVMDto.getVMUuid());

        return returnVal;
    }

    private Credentials transformToCredentials(final String username, final String password, final String endpoint)
    {
        return new Credentials(endpoint, password, username);
    }
}
