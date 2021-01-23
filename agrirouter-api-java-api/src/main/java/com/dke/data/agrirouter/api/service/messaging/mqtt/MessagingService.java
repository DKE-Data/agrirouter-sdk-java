package com.dke.data.agrirouter.api.service.messaging.mqtt;

import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;

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
  MqttAsyncMessageSendingResult sendAsync(T parameters);
}
