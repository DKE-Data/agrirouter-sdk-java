package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.DeleteMessageMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.DeleteMessageMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.DeleteMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseStatusChecker;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;
import java.util.Objects;
import org.apache.http.HttpStatus;

public class DeleteMessageServiceImpl
    implements DeleteMessageService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;

  public DeleteMessageServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public void send(DeleteMessageParameters parameters) {
    parameters.validate();

    String encodedMessage = encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(Collections.singletonList(encodedMessage));

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    int status = response.getNativeResponse().getStatus();
    if (!ResponseStatusChecker.isStatusInSuccessRange(status)) {
      this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK);
    }
  }

  private String encodeMessage(DeleteMessageParameters parameters) {
    String applicationMessageId = MessageIdService.generateMessageId();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageId);
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_DELETE);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    DeleteMessageMessageParameters deleteMessageMessageParameters =
        new DeleteMessageMessageParameters();
    deleteMessageMessageParameters.setMessageIds(
        Objects.requireNonNull(parameters.getMessageIds()));
    deleteMessageMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
    deleteMessageMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
    deleteMessageMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(FeedRequests.MessageDelete.getDescriptor().getFullName());
    payloadParameters.setValue(
        new DeleteMessageMessageContentFactory().message(deleteMessageMessageParameters));

    return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
  }
}
