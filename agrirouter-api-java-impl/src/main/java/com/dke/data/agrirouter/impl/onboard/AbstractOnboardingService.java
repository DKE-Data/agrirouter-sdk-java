package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;

public abstract class AbstractOnboardingService extends EnvironmentalService {

  protected AbstractOnboardingService(Environment environment) {
    super(environment);
  }

  public OnboardingRequest getOnboardRequest(
      String uuid,
      String applicationId,
      String certificationVersionId,
      String gatewayId,
      CertificationType certificationType) {
    OnboardingRequest onboardingRequest = new OnboardingRequest();
    onboardingRequest.setId(uuid);
    onboardingRequest.setApplicationId(applicationId);
    onboardingRequest.setCertificationVersionId(certificationVersionId);
    onboardingRequest.setGatewayId(gatewayId);
    onboardingRequest.setCertificateType(certificationType.getKey());
    onboardingRequest.setUTCTimestamp(UtcTimeService.inThePast(10).toString());
    onboardingRequest.setTimeZone(UtcTimeService.offset());
    return onboardingRequest;
  }
}
