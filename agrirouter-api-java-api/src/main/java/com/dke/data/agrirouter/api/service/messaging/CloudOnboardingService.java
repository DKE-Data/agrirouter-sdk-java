package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;

import java.util.List;

public interface CloudOnboardingService extends MessagingService<CloudOnboardingParameters> {

  List<OnboardingResponse> onboard(CloudOnboardingParameters parameters);
}
