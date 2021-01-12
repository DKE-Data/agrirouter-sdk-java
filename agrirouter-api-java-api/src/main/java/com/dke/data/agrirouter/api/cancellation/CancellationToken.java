package com.dke.data.agrirouter.api.cancellation;

/** Cancellation token to enable custom implementations integration within the solution. */
public interface CancellationToken {

  /**
   * Signals the polling that the operation can be cancelled.
   *
   * @return true if the operation can be cancelled, false otherwise.
   */
  boolean isNotCancelled();

  /**
   * Will be called if one step during the whole polling mechanism is done and the next iteration
   * will start afterwards.
   */
  void step();

  /** Will wait a dedicated amount of time before starting the next step. */
  void waitBeforeStartingNextStep();
}
