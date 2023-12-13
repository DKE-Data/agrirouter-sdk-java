package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.MessageConfirmationService;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

import java.util.Collections;

public class MessageConfirmationServiceImpl extends EnvironmentalService
        implements MessageConfirmationService, MessageSender, MessageEncoder, ResponseValidator {

    private final EncodeMessageService encodeMessageService;

    public MessageConfirmationServiceImpl(Environment environment) {
        super(environment);
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(MessageConfirmationParameters parameters) {
        parameters.validate();
        var encodedMessage = this.encode(parameters);
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        var response = this.sendMessage(sendMessageParameters);
        this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
        return encodedMessage.getApplicationMessageID();
    }

    @Override
    public HttpAsyncMessageSendingResult sendAsync(MessageConfirmationParameters parameters) {
        parameters.validate();
        var encodedMessage = this.encode(parameters);
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        var response =
                this.sendMessageAsync(sendMessageParameters);
        return new HttpAsyncMessageSendingResult(response, encodedMessage.getApplicationMessageID());
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
