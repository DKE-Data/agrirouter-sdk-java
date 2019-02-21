package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
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
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

public class ListEndpointsServiceImpl extends EnvironmentalService
    implements ListEndpointsService, MessageSender, ResponseValidator {

  private EncodeMessageService encodeMessageService;

  public ListEndpointsServiceImpl(Environment environment) {
    super(environment);
    encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(ListEndpointsParameters parameters) {

    EncodeMessageResponse encodedMessage = encodeMessage(parameters);

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.onboardingResponse = parameters.onboardingResponse;
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));

    sendMessage(sendMessageParameters);

    return encodedMessage.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(ListEndpointsParameters parameters) {

    String applicationMessageID = MessageIdService.generateMessageId();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    if (parameters.getUnFilteredList()) {
      messageHeaderParameters.technicalMessageType =
          TechnicalMessageType.DKE_LIST_ENDPOINTS_UNFILTERED;
    } else {
      messageHeaderParameters.technicalMessageType = TechnicalMessageType.DKE_LIST_ENDPOINTS;
    }
    messageHeaderParameters.mode = Request.RequestEnvelope.Mode.DIRECT;

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(Endpoints.ListEndpointsQuery.getDescriptor().getFullName());
    payloadParameters.value = new ListEndpointsMessageContentFactory().message(parameters);

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);

    return new EncodeMessageResponse(applicationMessageID, encodedMessage);
  }

  public String requestFullListFilteredByAppliedRoutings(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    listEndpointsParameters.technicalMessageType = TechnicalMessageType.NONE;
    listEndpointsParameters.onboardingResponse = onboardingResponse;
    listEndpointsParameters.setUnFilteredList(false);

    return this.send(listEndpointsParameters);
  }

  public String requestFullList(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    listEndpointsParameters.technicalMessageType = TechnicalMessageType.NONE;
    listEndpointsParameters.onboardingResponse = onboardingResponse;
    listEndpointsParameters.setUnFilteredList(true);

    return this.send(listEndpointsParameters);
  }
}
