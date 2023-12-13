package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.messaging.MessageSendingResponse;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

import java.util.concurrent.CompletableFuture;

/**
 * Base class to send messages.
 */
public class SendMessageServiceImpl
        implements SendMessageService<MessageSendingResponse>, ResponseValidator, MessageSender {

    @Override
    public void send(SendMessageParameters sendMessageParameters) {
        sendMessageParameters.validate();
        var response = this.sendMessage(sendMessageParameters);
        this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    }

    @Override
    public CompletableFuture<MessageSendingResponse> sendAsync(
            SendMessageParameters sendMessageParameters) {
        sendMessageParameters.validate();
        return this.sendMessageAsync(sendMessageParameters);
    }
}
