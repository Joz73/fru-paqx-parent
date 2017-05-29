/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.dto.ScaleIORemoveDto;
import com.dell.cpsd.paqx.fru.dto.ScaleIORemoveDataDto;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveData;
import com.dell.cpsd.storage.capabilities.api.SIONodeRemoveRequestMessage;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class SDSListDtoToRemoveScaleIOMessageTransformer
{
    public SIONodeRemoveRequestMessage transform(final ScaleIORemoveDto hostList)
    {
        if (hostList == null)
        {
            return null;
        }

        SIONodeRemoveRequestMessage returnVal = new SIONodeRemoveRequestMessage();
        returnVal.setApiPortNumber(Integer.toString(hostList.getServiceAPIPort()));
        returnVal.setIpAddress(hostList.getIpAddress());
        returnVal.setPassword(hostList.getPassword());
        returnVal.setServicePortNumber(Integer.toString(hostList.getServiceAPIPort()));
        returnVal.setUserName(hostList.getUserName());
        returnVal.setSIONodeRemoveData(transform(hostList.getScaleIORemoveDataDto()));
        return returnVal;
    }

    private SIONodeRemoveData transform(final ScaleIORemoveDataDto scaleIORemoveDataDto)
    {
        if (scaleIORemoveDataDto == null)
        {
            return null;
        }

        SIONodeRemoveData returnVal = new SIONodeRemoveData();
        returnVal.setMdmHosts(scaleIORemoveDataDto.getScaleIOMdmHosts());
        returnVal.setScaleioInterface(scaleIORemoveDataDto.getScaleIOInterface());
        returnVal.setScaleioVolumeName(scaleIORemoveDataDto.getScaleIOVolumeName());
        returnVal.setSdcHosts(scaleIORemoveDataDto.getScaleIOSdcHosts());
        returnVal.setSdsHosts(scaleIORemoveDataDto.getScaleIOSdsHosts());

        return returnVal;
    }
}
