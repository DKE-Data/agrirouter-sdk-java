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
import java.util.Objects;

public class MessageQueryHelper<SenderType> extends NonEnvironmentalService
    implements MessageSender<SenderType>, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final MessageSender messageSender;
  private final TechnicalMessageType technicalMessageType;

  public MessageQueryHelper(
      EncodeMessageService encodeMessageService,
      MessageSender messageSender,
      TechnicalMessageType technicalMessageType) {
    this.logMethodBegin();
    this.encodeMessageService = encodeMessageService;
    this.technicalMessageType = technicalMessageType;
    this.messageSender = messageSender;
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
    sendMessageParameters.setEncodeMessageResponse(encodedMessageResponse);

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

    final String applicationMessageID = MessageIdService.generateMessageId();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);

    messageHeaderParameters.setApplicationMessageSeqNo(1);
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

    return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
  }

  @Override
  public SenderType createSendMessageRequest(SendMessageParameters parameters) {
    return (SenderType) messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
