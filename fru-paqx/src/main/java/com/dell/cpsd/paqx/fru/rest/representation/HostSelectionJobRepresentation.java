/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.paqx.fru.rest.representation;

import com.dell.cpsd.paqx.fru.rest.domain.Job;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class HostSelectionJobRepresentation extends JobRepresentation
{

    private List<HostRepresentation> nodes = new ArrayList<>();

    public HostSelectionJobRepresentation(final Job job)
    {
        super(job);
    }

    public void setNodes(final List<HostRepresentation> nodes)
    {
        this.nodes = nodes;
    }

    public List<HostRepresentation> getNodes()
    {
        return nodes;
    }
}
