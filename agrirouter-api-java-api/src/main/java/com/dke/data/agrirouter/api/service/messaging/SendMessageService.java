package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;

import java.util.concurrent.CompletableFuture;

/** Service for sending a message */
public interface SendMessageService<T> {

  /**
   * Sending a message
   *
   * @param sendMessageParameters -
   */
  void send(SendMessageParameters sendMessageParameters);

  /**
   * Sending a message
   *
   * @param sendMessageParameters -
   * @return -
   */
  CompletableFuture<T> sendAsync(SendMessageParameters sendMessageParameters);
}
