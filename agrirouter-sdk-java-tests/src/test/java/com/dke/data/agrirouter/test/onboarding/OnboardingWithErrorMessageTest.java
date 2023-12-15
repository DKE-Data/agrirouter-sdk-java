package com.dke.data.agrirouter.test.onboarding;

import com.dke.data.agrirouter.api.enums.ApplicationType;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.exception.OnboardingException;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.onboard.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OnboardingWithErrorMessageTest extends AbstractIntegrationTest {

    @Test
    void
    givenInvalidRegistrationCodeWhenSendingTheOnboardingRequestThenThereShouldBeAnOnboardingExceptionWithFilledErrorMessage() {
        OnboardingService onboardingService = new OnboardingServiceImpl(getEnv());
        var onboardingParameters = new OnboardingParameters();
        onboardingParameters.setApplicationMessageId(MessageIdService.generateMessageId());
        onboardingParameters.setApplicationType(ApplicationType.APPLICATION);
        onboardingParameters.setApplicationId(farmingSoftware.getApplicationId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setCertificationVersionId(farmingSoftware.getCertificationVersionId());
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setUuid(UUID.randomUUID().toString());
        onboardingParameters.setRegistrationCode("INVALID_REGISTRATION_TOKEN");
        var onboardingException =
                Assertions.assertThrows(
                        OnboardingException.class, () -> onboardingService.onboard(onboardingParameters));
        Assertions.assertNotNull(onboardingException.getOnboardingError());
        Assertions.assertNotNull(onboardingException.getOnboardingError().getError());
        Assertions.assertEquals(401, onboardingException.getOnboardingError().getError().getCode());
        Assertions.assertEquals(
                "Bearer not found.", onboardingException.getOnboardingError().getError().getMessage());
    }

    private Environment getEnv() {
        return new QA() {
        };
    }
}
