package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;

public interface ListEndpointsService extends MessagingService<ListEndpointsParameters> {

  String requestFullListFilteredByAppliedRoutings(OnboardingResponse onboardingResponse);

  String requestFullList(OnboardingResponse onboardingResponse);
}
