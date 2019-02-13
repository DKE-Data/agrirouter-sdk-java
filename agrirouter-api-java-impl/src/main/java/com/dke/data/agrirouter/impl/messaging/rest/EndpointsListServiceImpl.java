package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.factories.impl.EndpointsListMessageContentFactory;
import com.dke.data.agrirouter.api.service.messaging.EndpointsListService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.EndpointsListParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EndpointsListServiceImpl extends EnvironmentalService
    implements EndpointsListService, MessageSender, ResponseValidator {
  private Logger LOGGER = LogManager.getLogger();

  private EncodeMessageService encodeMessageService;
  // private Logger logger;
  public EndpointsListServiceImpl(Environment environment) {
    super(environment);
    encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(EndpointsListParameters parameters) {

    TechnicalMessageType technicalMessageType = parameters.technicalMessageType;
    // there is no validation required
    // parameters.validate();

    EncodeMessageResponse encodedMessage = encodeMessage(parameters);

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.onboardingResponse = parameters.onboardingResponse;
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));
    

    sendMessage(sendMessageParameters);

    return encodedMessage.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(EndpointsListParameters parameters) {

    String applicationMessageID = UUID.randomUUID().toString();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    if (parameters.getUnFilteredList() == true) {
      messageHeaderParameters.technicalMessageType =
          TechnicalMessageType.DKE_LIST_ENDPOINTS_UNFILTERED;

    } else {
      messageHeaderParameters.technicalMessageType = TechnicalMessageType.DKE_LIST_ENDPOINTS;
    }
    messageHeaderParameters.mode = Request.RequestEnvelope.Mode.DIRECT;

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(Endpoints.ListEndpointsQuery.getDescriptor().getFullName());
    payloadParameters.value = new EndpointsListMessageContentFactory().message(parameters);

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);

    return new EncodeMessageResponse(applicationMessageID, encodedMessage);
  }

  public String requestFullListFiltered(OnboardingResponse onboardingResponse) {
    EndpointsListParameters endpointsListParameters = new EndpointsListParameters();
    endpointsListParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    //null marks, that there shall be no filtering for a specific message type
    endpointsListParameters.technicalMessageType = null;
    endpointsListParameters.onboardingResponse = onboardingResponse;
    endpointsListParameters.setUnFilteredList(false);

    return this.send(endpointsListParameters);
  }

  public String requestFullListUnFiltered(OnboardingResponse onboardingResponse) {
    EndpointsListParameters endpointsListParameters = new EndpointsListParameters();
    endpointsListParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE;
    //null marks, that there shall be no filtering for a specific message type
    endpointsListParameters.technicalMessageType = null;
    endpointsListParameters.onboardingResponse = onboardingResponse;
    endpointsListParameters.setUnFilteredList(true);

    return this.send(endpointsListParameters);
  }
}
