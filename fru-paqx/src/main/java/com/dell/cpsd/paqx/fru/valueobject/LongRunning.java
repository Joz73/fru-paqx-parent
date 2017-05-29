/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.valueobject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * TBD.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class LongRunning<A, R>
{
    private final CompletableFuture<A> acknowledgementPromise;
    private final CompletableFuture<R> completionPromise;

    public LongRunning(final CompletableFuture<A> acknowledgementPromise, final CompletableFuture<R> completionPromise)
    {
        this.acknowledgementPromise = acknowledgementPromise;
        this.completionPromise = completionPromise;
    }

    public LongRunning<A, R> onAcknowledged(Consumer<A> consumer)
    {
        acknowledgementPromise.thenAccept(consumer);
        return this;
    }
    

    public LongRunning<A, R> onCompleted(Consumer<R> onCompleteConsumer)
    {
        completionPromise.thenAccept(onCompleteConsumer);
        return this;
    }
}
