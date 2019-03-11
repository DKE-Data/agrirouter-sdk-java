package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.factories.impl.ListEndpointsMessageContentFactory;
import com.dke.data.agrirouter.api.service.messaging.ListEndpointsService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

public class ListEndpointsServiceImpl extends EnvironmentalService
    implements ListEndpointsService, MessageSender, ResponseValidator {

  private EncodeMessageService encodeMessageService;
  private MessageSender messageSender;

  /**
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     DeleteMessageServiceJSONImpl or DeleteMessageServiceProtobufImpl instead
   */
  @Deprecated
  public ListEndpointsServiceImpl(Environment environment) {
    this(environment, new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }

  public ListEndpointsServiceImpl(
      Environment environment,
      MessageSender messageSender,
      EncodeMessageService encodeMessageService) {
    super(environment);
    this.messageSender = messageSender;
    this.encodeMessageService = encodeMessageService;
  }

  @Override
  public String send(ListEndpointsParameters parameters) {

    EncodeMessageResponse encodedMessage = this.encodeMessage(parameters);

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodeMessageResponse(encodedMessage);

    this.sendMessage(sendMessageParameters);

    return encodedMessage.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(ListEndpointsParameters parameters) {

    String applicationMessageID = MessageIdService.generateMessageId();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    if (parameters.getUnfilteredList()) {
      messageHeaderParameters.setTechnicalMessageType(
          TechnicalMessageType.DKE_LIST_ENDPOINTS_UNFILTERED);
    } else {
      messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_LIST_ENDPOINTS);
    }
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(Endpoints.ListEndpointsQuery.getDescriptor().getFullName());
    payloadParameters.setValue(new ListEndpointsMessageContentFactory().message(parameters));

    return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
  }

  public String requestFullListFilteredByAppliedRoutings(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    listEndpointsParameters.technicalMessageType = TechnicalMessageType.EMPTY;
    listEndpointsParameters.onboardingResponse = onboardingResponse;
    listEndpointsParameters.setUnfilteredList(false);

    return this.send(listEndpointsParameters);
  }

  public String requestFullList(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    listEndpointsParameters.technicalMessageType = TechnicalMessageType.EMPTY;
    listEndpointsParameters.onboardingResponse = onboardingResponse;
    listEndpointsParameters.setUnfilteredList(true);

    return this.send(listEndpointsParameters);
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return this.messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
