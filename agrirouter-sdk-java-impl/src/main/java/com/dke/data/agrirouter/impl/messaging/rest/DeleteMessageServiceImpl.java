package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.DeleteMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.DeleteAllMessagesParameterCreator;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

import java.util.Collections;

public class DeleteMessageServiceImpl
        implements DeleteMessageService,
        MessageSender,
        ResponseValidator,
        MessageEncoder,
        DeleteAllMessagesParameterCreator {

    private final EncodeMessageService encodeMessageService;

    public DeleteMessageServiceImpl() {
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(DeleteMessageParameters parameters) {
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
    public HttpAsyncMessageSendingResult sendAsync(DeleteMessageParameters parameters) {
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

    @Override
    public String sendMessageToDeleteAll(OnboardingResponse onboardingResponse) {
        final var deleteMessageParameters =
                createMessageParametersToDeleteAllMessages(onboardingResponse);
        return send(deleteMessageParameters);
    }

    @Override
    public HttpAsyncMessageSendingResult sendMessageToDeleteAllAsync(
            OnboardingResponse onboardingResponse) {
        final var deleteMessageParameters =
                createMessageParametersToDeleteAllMessages(onboardingResponse);
        return sendAsync(deleteMessageParameters);
    }
}
