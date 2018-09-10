package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;

import java.util.List;

/**
 * Service for sending a message
 */
public interface SendMessageService {

    /**
     * Sending a message
     *
     * @param sendMessageParameters -
     */
    void send(SendMessageParameters sendMessageParameters);

}
