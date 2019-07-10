package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.ListEndpointsService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

public class ListEndpointsServiceImpl extends EnvironmentalService
    implements ListEndpointsService, MessageSender, MessageEncoder, ResponseValidator {

  private EncodeMessageService encodeMessageService;

  public ListEndpointsServiceImpl(Environment environment) {
    super(environment);
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(ListEndpointsParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessage = this.encode(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));
    this.sendMessage(sendMessageParameters);
    return encodedMessage.getApplicationMessageID();
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
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
