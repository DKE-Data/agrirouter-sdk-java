package com.dke.data.agrirouter.impl.messaging.helper;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.messaging.MessageSendingResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class MessageQueryHelperService extends NonEnvironmentalService
        implements MessageSender, MessageEncoder, ResponseValidator, QueryAllMessagesParameterCreator {

    private final EncodeMessageService encodeMessageService;
    private final TechnicalMessageType technicalMessageType;

    public MessageQueryHelperService(
            EncodeMessageService encodeMessageService, TechnicalMessageType technicalMessageType) {
        this.logMethodBegin();
        this.encodeMessageService = encodeMessageService;
        this.technicalMessageType = technicalMessageType;
        this.logMethodEnd();
    }

    public String send(MessageQueryParameters parameters) {
        this.logMethodBegin(parameters);

        this.getNativeLogger().trace("Validate parameters.");
        parameters.validate();

        this.getNativeLogger().trace("Encode message.");
        var encodedMessageResponse = this.encode(this.technicalMessageType, parameters);

        this.getNativeLogger().trace("Build message parameters.");
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessageResponse.getEncodedMessage()));

        this.getNativeLogger().trace("Send and fetch message response.");
        var response = this.sendMessage(sendMessageParameters);

        this.getNativeLogger().trace("Validate message response.");
        this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());

        this.logMethodEnd();
        return encodedMessageResponse.getApplicationMessageID();
    }

    public HttpAsyncMessageSendingResult sendAsync(MessageQueryParameters parameters) {
        this.logMethodBegin(parameters);

        this.getNativeLogger().trace("Validate parameters.");
        parameters.validate();

        this.getNativeLogger().trace("Encode message.");
        var encodedMessageResponse = this.encode(this.technicalMessageType, parameters);

        this.getNativeLogger().trace("Build message parameters.");
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessageResponse.getEncodedMessage()));

        this.getNativeLogger().trace("Send and fetch message response.");
        var response =
                this.sendMessageAsync(sendMessageParameters);

        this.logMethodEnd();
        return new HttpAsyncMessageSendingResult(
                response, encodedMessageResponse.getApplicationMessageID());
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
