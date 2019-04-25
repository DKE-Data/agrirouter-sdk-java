package com.dke.data.agrirouter.impl.messaging.helper;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.MessageQueryMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;
import java.util.Objects;

public class MessageQueryService extends NonEnvironmentalService
    implements MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final TechnicalMessageType technicalMessageType;

  public MessageQueryService(
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
    EncodeMessageResponse encodedMessageResponse = this.encodeMessage(parameters);

    this.getNativeLogger().trace("Build message parameters.");
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessageResponse.getEncodedMessage()));

    this.getNativeLogger().trace("Send and fetch message response.");
    MessageSender.MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.getNativeLogger().trace("Validate message response.");
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());

    this.logMethodEnd();
    return encodedMessageResponse.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(MessageQueryParameters parameters) {
    this.logMethodBegin(parameters);

    this.getNativeLogger().trace("Build message header parameters.");
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));
    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());
    messageHeaderParameters.setTechnicalMessageType(this.technicalMessageType);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    this.getNativeLogger().trace("Build message query parameters.");
    MessageQueryMessageParameters messageQueryMessageParameters =
        new MessageQueryMessageParameters();
    messageQueryMessageParameters.setMessageIds(Objects.requireNonNull(parameters.getMessageIds()));
    messageQueryMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
    messageQueryMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
    messageQueryMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());

    this.getNativeLogger().trace("Build message payload parameters.");
    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(FeedRequests.MessageQuery.getDescriptor().getFullName());
    payloadParameters.setValue(
        new MessageQueryMessageContentFactory().message(messageQueryMessageParameters));

    this.getNativeLogger().trace("Encode message.");
    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);

    this.logMethodEnd(encodedMessage);
    return new EncodeMessageResponse(applicationMessageID, encodedMessage);
  }
}
