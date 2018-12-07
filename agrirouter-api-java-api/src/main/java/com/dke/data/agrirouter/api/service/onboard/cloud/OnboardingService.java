package com.dke.data.agrirouter.api.service.onboard.cloud;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;

public interface OnboardingService extends MessageDecoder<CloudVirtualizedAppRegistration.OnboardingResponse> {
}
