/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.domain;

import com.dell.cpsd.paqx.fru.rest.dto.EndpointCredentials;
import com.dell.cpsd.paqx.fru.valueobject.LongRunning;
import com.dell.cpsd.paqx.fru.rest.representation.HostRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Workflow step.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class Job
{
    private UUID                id;
    private String              workflow;
    private String              currentStep;
    private int                 currentStepNumber;
    private EndpointCredentials rackhdCredentials;
    private EndpointCredentials coprhdCredentials;
    private EndpointCredentials vcenterCredentials;
    private EndpointCredentials scaleIOCredentials;
    private EndpointCredentials scaleIOMDMCredentials;
    private final Map<String, Set<String>> longRunningTasks = new ConcurrentHashMap<>();
    {
    };

    private HostRepresentation selectedHostRepresentation;

    private List<String> selectedVMsToDestroy;

    public Job(final UUID id, final String workflow, final String currentStep) {
        this.id = id;
        this.workflow = workflow;
        this.currentStep = currentStep;
        this.currentStepNumber = 1;
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

    public void changeToNextStep(final String nextStep)
    {
        currentStep = nextStep;
    }

    public int getCurrentStepNumber()
    {
        return currentStepNumber;
    }

    public void addRackhdCredentials(final EndpointCredentials rackhdCredentials)
    {
        this.rackhdCredentials = rackhdCredentials;
    }

    public EndpointCredentials getRackhdCredentials()
    {
        return rackhdCredentials;
    }

    public EndpointCredentials getCoprhdCredentials()
    {
        return coprhdCredentials;
    }

    public void addCoprhdCredentials(final EndpointCredentials coprhdCredentials)
    {
        this.coprhdCredentials = coprhdCredentials;
    }

    public void addScaleIOCredentials(final EndpointCredentials scaleIOCredentials)
    {
        this.scaleIOCredentials = scaleIOCredentials;
    }

    public void addScaleIOMDMCredentials(final EndpointCredentials scaleIOMDMCredentials)
    {
        this.scaleIOMDMCredentials = scaleIOMDMCredentials;
    }

    public EndpointCredentials getScaleIOCredentials()
    {
        return scaleIOCredentials;
    }

    public EndpointCredentials getScaleIOMDMCredentials()
    {
        return scaleIOMDMCredentials;
    }

    public EndpointCredentials getVcenterCredentials()
    {
        return vcenterCredentials;
    }

    public void addVcenterCredentials(final EndpointCredentials vcenterCredentials)
    {
        this.vcenterCredentials = vcenterCredentials;
    }

    public void setSelectedHostRepresentation(final HostRepresentation selectedHostRepresentation)
    {
        this.selectedHostRepresentation = selectedHostRepresentation;
    }

    public void setSelectedVMsToDestroy(final List<String> selectedVMsToDestroy)
    {
        this.selectedVMsToDestroy = selectedVMsToDestroy;
    }

    public HostRepresentation getSelectedHostRepresentation()
    {
        return selectedHostRepresentation;
    }

    public List<String> getSelectedVMsToDestroy()
    {
        return selectedVMsToDestroy;
    }

    public void addLongRunningTask(final String longRunningStep, final LongRunning longRunning)
    {
        final String acknowledgeKey = String.format("%s:%s", longRunning, "acknowledge");
        final String completeKey = String.format("%s:%s", longRunning, "complete");
        longRunningTasks.putIfAbsent(longRunningStep, new HashSet<>(Arrays.asList(acknowledgeKey, completeKey)));

        longRunning.onAcknowledged((result) -> longRunningTasks.computeIfPresent(longRunningStep, (key, outstandingSteps) ->  outstandingSteps.stream().filter(
                step -> !acknowledgeKey.equals(step)).collect(
                Collectors.toSet()) ));

        longRunning.onCompleted((result) -> longRunningTasks.computeIfPresent(longRunningStep, (key, outstandingSteps) ->  outstandingSteps.stream().filter(
                step -> !completeKey.equals(step)).collect(
                Collectors.toSet()) ));
    }

    public boolean areAnyLongRunningActionsInProgress()
    {
        for (final String longRunningTask : longRunningTasks.keySet())
        {
            final Set<String> outstandingTasks = longRunningTasks.get(longRunningTask);
            if (outstandingTasks != null && !outstandingTasks.isEmpty())
            {
                return true;
            }
        }
        return false;
    }
}
