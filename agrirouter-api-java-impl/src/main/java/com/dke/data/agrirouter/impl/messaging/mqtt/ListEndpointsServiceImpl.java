package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.factories.impl.ListEndpointsMessageContentFactory;
import com.dke.data.agrirouter.api.service.messaging.ListEndpointsService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import java.util.Collections;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ListEndpointsServiceImpl extends MqttService
    implements ListEndpointsService, MessageBodyCreator {

  private EncodeMessageService encodeMessageService;

  public ListEndpointsServiceImpl(IMqttClient mqttClient) {
    super(mqttClient);
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(ListEndpointsParameters parameters) {
    EncodedMessage encodedMessage = this.encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));
    String messageAsJson = this.createMessageBody(sendMessageParameters);
    byte[] payload = messageAsJson.getBytes();
    try {
      this.getMqttClient()
          .publish(
              parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
              new MqttMessage(payload));
      return encodedMessage.getApplicationMessageID();
    } catch (MqttException e) {
      throw new CouldNotSendMqttMessageException(e);
    }
  }

  private EncodedMessage encodeMessage(ListEndpointsParameters parameters) {

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

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);

    return new EncodedMessage(applicationMessageID, encodedMessage);
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
}
