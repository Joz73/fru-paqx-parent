/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.dto;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class VCenterHostPowerOperationStatus {
    private final String status;

    public VCenterHostPowerOperationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
