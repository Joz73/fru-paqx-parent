/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.rest.domain;

import com.dell.cpsd.paqx.fru.valueobject.LongRunning;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * TBD.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class JobTest
{
    private final CompletableFuture acknowledgeCompletableFuture = new CompletableFuture<String>();
    private final CompletableFuture completeCompletableFuture = new CompletableFuture<Integer>();

    private final Job job = new Job(UUID.randomUUID(), "workflow", "currentStep");
    private final LongRunning longRunningTask = new LongRunning(acknowledgeCompletableFuture, completeCompletableFuture);

    @Test
    public void longRunningTasks()
    {
        job.addLongRunningTask("currentStep", longRunningTask);
        assertTrue("expected long running task in progress", job.areAnyLongRunningActionsInProgress());
    }

    @Test
    public void aLongRunningTaskCanBePartiallyCompleted()
    {
        job.addLongRunningTask("currentStep", longRunningTask);
        acknowledgeCompletableFuture.complete("completed");
        assertTrue("expected long running task in progress", job.areAnyLongRunningActionsInProgress());
    }



    @Test
    public void aLongRunningTaskCanBeCompleted()
    {
        job.addLongRunningTask("currentStep", longRunningTask);
        acknowledgeCompletableFuture.complete("completed");
        completeCompletableFuture.complete(Integer.valueOf(1));
        assertFalse("expected long running task in progress", job.areAnyLongRunningActionsInProgress());
    }
}
