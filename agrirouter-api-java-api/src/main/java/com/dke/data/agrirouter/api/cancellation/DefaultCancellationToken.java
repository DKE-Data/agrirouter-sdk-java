package com.dke.data.agrirouter.api.cancellation;

/**
 * Default - time based - implementation of the cancellation token.
 */
public class DefaultCancellationToken implements CancellationToken {

    private int nrOfTries;
    private final int maxTries;
    private final long timeToWait;
    private boolean wasInterrupted;

    public DefaultCancellationToken(int maxTries, long timeToWait) {
        this.nrOfTries = 0;
        this.maxTries = maxTries;
        this.timeToWait = timeToWait;
        this.wasInterrupted = false;
    }

    @Override
    public boolean isNotCancelled() {
        return nrOfTries < maxTries && !wasInterrupted;
    }

    @Override
    public void step() {
        nrOfTries++;
    }

    @Override
    public void waitBeforeStartingNextStep() {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException nop) {
            wasInterrupted = true;
        }
    }
}
