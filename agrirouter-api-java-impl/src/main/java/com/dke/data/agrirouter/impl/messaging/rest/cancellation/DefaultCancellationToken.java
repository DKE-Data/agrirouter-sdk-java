package com.dke.data.agrirouter.impl.messaging.rest.cancellation;

/** Default - time based - implementation of the cancellation token. */
public class DefaultCancellationToken implements CancellationToken {

  private int nrOfTries;
  private final int maxTries;
  private final long timeToWait;

  public DefaultCancellationToken(int maxTries, long timeToWait) {
    this.nrOfTries = 0;
    this.maxTries = maxTries;
    this.timeToWait = timeToWait;
  }

  @Override
  public boolean isNotCancelled() {
    return nrOfTries < maxTries;
  }

  @Override
  public void step() {
    nrOfTries++;
  }

  @Override
  public void waitBeforeStartingTheNextStep() {
    try {
      Thread.sleep(timeToWait);
    } catch (InterruptedException nop) {
      // NOP
    }
  }
}
