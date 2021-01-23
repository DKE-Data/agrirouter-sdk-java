package com.dke.data.agrirouter.api.service.messaging.http;

import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;

/**
 * Centralized service interface.
 *
 * @param <T> The type of parameters needed to send messages.
 */
public interface MessagingService<T> {

  /**
   * Send a synchronous message.
   *
   * @param parameters -
   * @return The ID of the message.
   */
  String send(T parameters);

  /**
   * Send a asynchronous message.
   *
   * @param parameters -
   * @return The completable future containing the possible result.
   */
  HttpAsyncMessageSendingResult sendAsync(T parameters);
}
