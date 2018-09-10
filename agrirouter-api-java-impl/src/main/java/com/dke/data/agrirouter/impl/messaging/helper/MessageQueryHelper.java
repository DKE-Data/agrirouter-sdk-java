package com.dke.data.agrirouter.impl.messaging.helper;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.MessageQueryMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.inner.Message;
import com.dke.data.agrirouter.impl.common.MessageCreationService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Objects;

public class MessageQueryHelper implements MessageSender, ResponseValidator {

    private final EncodeMessageService encodeMessageService;
    private final TechnicalMessageType technicalMessageType;

    public MessageQueryHelper(EncodeMessageService encodeMessageService, TechnicalMessageType technicalMessageType) {
        this.encodeMessageService = encodeMessageService;
        this.technicalMessageType = technicalMessageType;
    }

    public void send(MessageQueryParameters parameters) {
        parameters.validate();

        String messageId = MessageIdService.generateMessageId();

        String encodedMessage = encodeMessage(parameters);
        List<Message> messages = MessageCreationService.create(messageId, encodedMessage);

        SendMessageParameters sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setMessages(messages);

        MessageSender.MessageSenderResponse response = this.sendMessage(sendMessageParameters);

        this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK);
    }

    private String encodeMessage(MessageQueryParameters parameters) {
        String applicationMessageId = MessageIdService.generateMessageId();

        MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setApplicationMessageId(applicationMessageId);
        messageHeaderParameters.setApplicationMessageSeqNo(1);
        messageHeaderParameters.setTechnicalMessageType(this.technicalMessageType);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        MessageQueryMessageParameters messageQueryMessageParameters = new MessageQueryMessageParameters();
        messageQueryMessageParameters.setMessageIds(Objects.requireNonNull(parameters.getMessageIds()));
        messageQueryMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
        messageQueryMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
        messageQueryMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());

        PayloadParameters payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(FeedRequests.MessageQuery.getDescriptor().getFullName());
        payloadParameters.setValue(new MessageQueryMessageContentFactory().message(messageQueryMessageParameters));

        return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    }


}
