package com.dke.data.agrirouter.api.cancellation;

/**
 * Non-retry implementation of the cancellation token.
 */
@SuppressWarnings("unused")
public class NonRetryCancellationToken implements CancellationToken {

    public NonRetryCancellationToken() {
    }

    @Override
    public boolean isNotCancelled() {
        return true;
    }

    @Override
    public void nextStep() {
        // Do nothing
    }

    @Override
    public void waitIfNotCancelled() {
        // Do nothing
    }
}
