/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.representation;

import com.dell.cpsd.paqx.fru.rest.domain.Job;
import org.springframework.http.HttpMethod;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@XmlRootElement
public class JobRepresentation
{
    private UUID   id;
    private String workflow;
    private String currentStep; // TODO: query remove?
    private int currentStepNumber     = -1;
    private int expectedNumberOfSteps = -1;
    private String lastResponse;

    private Set<LinkRepresentation> links = new HashSet<>();

    public JobRepresentation(final Job job)
    {
        this.id = job.getId();
        this.workflow = job.getWorkflow();
        this.currentStep = job.getCurrentStep();
    }

    public UUID getId()
    {
        return id;
    }

    public String getCurrentStep()
    {
        return currentStep;
    }

    public String getWorkflow()
    {
        return workflow;
    }

    public int getExpectedNumberOfSteps()
    {
        return expectedNumberOfSteps;
    }

    public void setExpectedNumberOfSteps(final int expectedNumberOfSteps)
    {
        this.expectedNumberOfSteps = expectedNumberOfSteps;
    }

    public int getCurrentStepNumber()
    {
        return currentStepNumber;
    }

    public void setCurrentStepNumber(final int currentStepNumber)
    {
        this.currentStepNumber = currentStepNumber;
    }

    public void changeToNextStep(final String nextStep)
    {
        currentStep = nextStep;
    }

    public Set<LinkRepresentation> getLinks()
    {
        return links;
    }

    public void addLink(Link link)
    {
        addLink(link, 0);
    }

    public void addLink(Link link, int nextStepDelay)
    {
        links.add(LinkRepresentation.from(link, HttpMethod.POST.name(), nextStepDelay));
    }

    public void addLink(Link link, String httpMethod)
    {
        addLink(link, httpMethod, 0);
    }

    public void addLink(Link link, String httpMethod, int nextStepDelay)
    {
        links.add(LinkRepresentation.from(link, httpMethod, nextStepDelay));
    }

    public String getLastResponse()
    {
        return lastResponse;
    }

    public void setLastResponse(final String lastResponse)
    {
        this.lastResponse = lastResponse;
    }
}
