package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.SubscriptionOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.factories.impl.SubscriptionMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.SetSubscriptionService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SetSubscriptionParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SetSubscriptionServiceImpl extends EnvironmentalService
    implements SetSubscriptionService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;

  public SetSubscriptionServiceImpl(Environment environment) {
    super(environment);
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(SetSubscriptionParameters parameters) {
    parameters.validate();

    EncodeMessageResponse encodeMessageResponse = encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodeMessageResponse.getEncodedMessage()));

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodeMessageResponse.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(SetSubscriptionParameters parameters) {
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
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_SUBSCRIPTION);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    SubscriptionMessageParameters subscriptionList = new SubscriptionMessageParameters();
    subscriptionList.setList(new ArrayList<>());

    for (SetSubscriptionParameters.Subscription entry : parameters.getSubscriptionParameters()) {
      SubscriptionMessageParameters.SubscriptionMessageEntry messageTypeSubscriptionItem =
          new SubscriptionMessageParameters.SubscriptionMessageEntry();
      messageTypeSubscriptionItem.setTechnicalMessageType(entry.getTechnicalMessageType());
      messageTypeSubscriptionItem.setDdis(entry.getDdis());
      messageTypeSubscriptionItem.setPosition(entry.getPosition());
      subscriptionList.getList().add(messageTypeSubscriptionItem);
    }

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(SubscriptionOuterClass.Subscription.getDescriptor().getFullName());

    ByteString messageValue = new SubscriptionMessageContentFactory().message(subscriptionList);
    payloadParameters.setValue(messageValue);

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    return new EncodeMessageResponse(applicationMessageID, encodedMessage);
  }
}
