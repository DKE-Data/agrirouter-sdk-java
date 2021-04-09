package com.dke.data.agrirouter.convenience.mqtt.service.messaging;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.mqtt.ListEndpointsService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.impl.messaging.mqtt.ListEndpointsServiceImpl;
import org.eclipse.paho.client.mqttv3.IMqttClient;

/** Additional functions to list endpoints. */
public class ListEndpointsFunctionsService {

  private final ListEndpointsService listEndpointsService;

  public ListEndpointsFunctionsService(IMqttClient iMqttClient) {
    this.listEndpointsService = new ListEndpointsServiceImpl(iMqttClient);
  }

  /**
   * List all endpoints filtered by application routing.
   *
   * @param onboardingResponse -
   * @return -
   */
  public String requestFullListFilteredByAppliedRoutings(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
    listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
    listEndpointsParameters.setOnboardingResponse(onboardingResponse);
    listEndpointsParameters.setUnfilteredList(false);

    return this.listEndpointsService.send(listEndpointsParameters);
  }

  /**
   * List all endpoints.
   *
   * @param onboardingResponse -
   * @return -
   */
  public String requestFullList(OnboardingResponse onboardingResponse) {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
    listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
    listEndpointsParameters.setOnboardingResponse(onboardingResponse);
    listEndpointsParameters.setUnfilteredList(true);

    return this.listEndpointsService.send(listEndpointsParameters);
  }
}
