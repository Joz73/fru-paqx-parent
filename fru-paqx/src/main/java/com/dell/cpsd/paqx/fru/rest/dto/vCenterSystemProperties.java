/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto;

import com.dell.cpsd.paqx.fru.rest.dto.vcenter.discovery.DataCenterDto;

import java.util.List;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
public class vCenterSystemProperties
{
    private List<DataCenterDto> dataCenters;

    public void setDataCenters(final List<DataCenterDto> dataCenters)
    {
        this.dataCenters = dataCenters;
    }

    public List<DataCenterDto> getDataCenters()
    {
        return dataCenters;
    }
}
