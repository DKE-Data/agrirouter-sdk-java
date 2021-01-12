package com.dke.data.agrirouter.api.cancellation;

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
  public boolean waitBeforeStartingNextStep() {
    try {
      Thread.sleep(timeToWait);
      return true;
    } catch (InterruptedException nop) {
      return onWaitBeforeStartingNextStepSleepThrowsError();
    }
  }

  /**
   * Will be called when the sleep functionality breaks
   * Shall return true to continue the loop of requests
   **/
  private boolean onWaitBeforeStartingNextStepSleepThrowsError() {
    System.out.println("Sleep functionality in FetchMessage Command failed");
    //By Default, we continue anyway
    return true;
  }

}
