package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.service.messaging.MessageSenderResponse;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import org.apache.http.HttpStatus;

public class SendMessageServiceImpl
        implements SendMessageService, ResponseValidator, MessageSender {

    @Override
    public void send(SendMessageParameters parameters) {
        MessageSenderResponse response = this.sendWithoutValidation(parameters);
        this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK);
    }

    @Override
    public MessageSenderResponse sendWithoutValidation(SendMessageParameters parameters) {
        parameters.validate();
        return this.sendMessage(parameters);
    }

}
