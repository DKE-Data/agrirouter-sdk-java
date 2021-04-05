package com.dke.data.agrirouter.convenience.rest.service.messaging;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.http.ListEndpointsService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.impl.messaging.rest.ListEndpointsServiceImpl;

/** Additional functions to list endpoints. */
public class ListEndpointsFunctionsService {

  private final ListEndpointsService listEndpointsService;

  public ListEndpointsFunctionsService(Environment environment) {
    this.listEndpointsService = new ListEndpointsServiceImpl(environment);
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
    listEndpointsParameters.setTechnicalMessageType(TechnicalMessageType.EMPTY);
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
    listEndpointsParameters.setTechnicalMessageType(TechnicalMessageType.EMPTY);
    listEndpointsParameters.setOnboardingResponse(onboardingResponse);
    listEndpointsParameters.setUnfilteredList(true);

    return this.listEndpointsService.send(listEndpointsParameters);
  }
}
