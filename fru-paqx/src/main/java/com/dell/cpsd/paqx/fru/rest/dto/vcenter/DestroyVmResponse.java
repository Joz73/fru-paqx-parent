/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class DestroyVmResponse
{
    private final String status;

    public DestroyVmResponse(final String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        return "DestroyVmResponse{" + "status='" + status + '\'' + '}';
    }
}

