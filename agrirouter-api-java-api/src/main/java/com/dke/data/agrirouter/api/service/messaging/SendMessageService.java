package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;

/** Service for sending a message */
public interface SendMessageService {

  /**
   * Sending a message
   *
   * @param sendMessageParameters -
   */
  String send(SendMessageParameters sendMessageParameters);
}
