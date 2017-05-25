/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service;

import com.dell.cpsd.paqx.fru.rest.domain.Job;
import com.dell.cpsd.paqx.fru.rest.repository.JobRepository;
import com.dell.cpsd.paqx.fru.valueobject.NextStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {
    private final JobRepository jobRepository;
    private final Map<String, NextStep> workflowSteps;

    @Autowired
    public WorkflowServiceImpl(final JobRepository jobRepository) {
        this.jobRepository = jobRepository;

        workflowSteps = new HashMap<>();
        workflowSteps.put("workflowInitiated", new NextStep("captureRackHDEndpoint"));
        workflowSteps.put("captureRackHDEndpoint", new NextStep("captureCoprHDEndpoint"));
        workflowSteps.put("captureCoprHDEndpoint", new NextStep("capturevCenterEndpoint"));
        workflowSteps.put("capturevCenterEndpoint", new NextStep("captureScaleIOEndpoint"));
        workflowSteps.put("captureScaleIOEndpoint", new NextStep("startScaleIODataCollection"));
        workflowSteps.put("startScaleIODataCollection", new NextStep("startvCenterDataCollection"));
        workflowSteps.put("startvCenterDataCollection", new NextStep("getSystemList"));
        workflowSteps.put("getSystemList", new NextStep("selectNodeForRemoval"));
        workflowSteps.put("selectNodeForRemoval", new NextStep("captureScaleIOMDMCredentials"));
        workflowSteps.put("captureScaleIOMDMCredentials", new NextStep("startSIORemoveWorkflow"));
        workflowSteps.put("startSIORemoveWorkflow", new NextStep("destroyScaleIOVM"));
        workflowSteps.put("destroyScaleIOVM", new NextStep("enterMaintenanceMode"));
        workflowSteps.put("enterMaintenanceMode", new NextStep("removeHostFromVCenter"));
        workflowSteps.put("removeHostFromVCenter", new NextStep("rebootHostForDiscovery"));
        workflowSteps.put("rebootHostForDiscovery", new NextStep("powerOffEsxiHostForRemoval"));
        workflowSteps.put("powerOffEsxiHostForRemoval", new NextStep("instructPhysicalRemoval"));
        workflowSteps.put("instructPhysicalRemoval", new NextStep("presentSystemListForAddition"));
        workflowSteps.put("presentSystemListForAddition", new NextStep("configureDisksRackHD"));
        workflowSteps.put("configureDisksRackHD", new NextStep("installEsxi"));
        workflowSteps.put("installEsxi", new NextStep("addHostTovCenter"));
        workflowSteps.put("addHostTovCenter", new NextStep("installSIOVib"));
        workflowSteps.put("installSIOVib", new NextStep("exitVCenterMaintenanceMode"));
        workflowSteps.put("exitVCenterMaintenanceMode", new NextStep("deploySVM"));
        workflowSteps.put("deploySVM", new NextStep("startSIOAddWorkflow"));
        workflowSteps.put("startSIOAddWorkflow", new NextStep("mapSIOVolumesToHost"));
        workflowSteps.put("mapSIOVolumesToHost", new NextStep("completed", true));
        workflowSteps.put("completed", null);
    }

    @Override
    public Job createWorkflow(final String workflowType) {
        final Job job = new Job(UUID.randomUUID(), workflowType, "workflowInitiated");
        jobRepository.save(job);
        return job;
    }

    @Override
    public NextStep findNextStep(final String workflowType, final String currentStep) {
        return workflowSteps.get(currentStep);
    }

    @Override
    public Job[] findActiveJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job findJob(final UUID jobId) {
        return jobRepository.find(jobId);
    }

    @Override
    public Job advanceToNextStep(final Job job, String currentStep) {
        final NextStep nextStep = findNextStep(job.getWorkflow(), currentStep);
        job.changeToNextStep(nextStep.getNextStep());
        jobRepository.save(job);
        return job;
    }

}
