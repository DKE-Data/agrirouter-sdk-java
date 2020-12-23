package com.dke.data.agrirouter.api.service.messaging;

/**
 * Centralized service interface.
 * @param <T> The type of parameters needed to send messages.
 * @param <R> The type of result the asynchronous messaging will deliver.
 */
public interface MessagingService<T,R> {

  /**
   * Send a synchronous message.
   * @param parameters -
   * @return The ID of the message.
   */
  String send(T parameters);

  /**
   * Send a asynchronous message.
   * @param parameters -
   * @return The completable future containing the possible result.
   */
  R sendAsync(T parameters);

}
