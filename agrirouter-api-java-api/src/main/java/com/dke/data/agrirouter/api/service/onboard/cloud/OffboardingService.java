package com.dke.data.agrirouter.api.service.onboard.cloud;

import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;

public interface OffboardingService {
  int MAX_TRIES_BEFORE_FAILURE = 10;
  long DEFAULT_INTERVAL = 500;

  void offboard(CloudOffboardingParameters parameters);
}
