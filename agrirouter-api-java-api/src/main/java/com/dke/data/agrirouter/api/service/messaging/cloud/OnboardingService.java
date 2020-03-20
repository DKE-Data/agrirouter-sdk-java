package com.dke.data.agrirouter.api.service.messaging.cloud;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import java.util.List;

public interface OnboardingService
    extends MessageDecoder<CloudVirtualizedAppRegistration.OnboardingResponse> {

  List<OnboardingResponse> onboard(CloudOnboardingParameters parameters);
}
