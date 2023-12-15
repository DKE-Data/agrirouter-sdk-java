package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.CloudOnboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

import java.util.Collections;

public class CloudOnboardingServiceImpl
        implements CloudOnboardingService, MessageSender, ResponseValidator, MessageEncoder {

    private final EncodeMessageService encodeMessageService;

    public CloudOnboardingServiceImpl() {
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(CloudOnboardingParameters parameters) {
        parameters.validate();
        var encodedMessage = this.encode(parameters);
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        var response = this.sendMessage(sendMessageParameters);
        this.assertStatusCodeIsValid(response.getNativeResponse().getStatus());
        return encodedMessage.getApplicationMessageID();
    }

    @Override
    public HttpAsyncMessageSendingResult sendAsync(CloudOnboardingParameters parameters) {
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
