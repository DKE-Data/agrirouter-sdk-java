package com.dke.data.agrirouter.test.onboarding;

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.service.onboard.secured.AuthorizationRequestService;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.impl.onboard.secured.AuthorizationRequestServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstration how to use the authorization request service.
 */
public class AuthorizationRequestServiceTest extends AbstractIntegrationTest {

    @Test
    void givenValidParameterWhenCreatingTheUrlThenTheUrlShouldBeGeneratedForFarmingSoftware() {
        AuthorizationRequestService authorizationRequestService =
                new AuthorizationRequestServiceImpl(new QA() {
                });
        AuthorizationRequestParameters authorizationRequestParameters =
                new AuthorizationRequestParameters();
        authorizationRequestParameters.setApplicationId(farmingSoftware.getApplicationId());
        authorizationRequestParameters.setResponseType(SecuredOnboardingResponseType.ONBOARD);
        authorizationRequestParameters.setState("my-custom-state-to-identify-the-request");
        final String authorizationRequestURL =
                authorizationRequestService.getAuthorizationRequestURL(authorizationRequestParameters);
        assertNotNull(authorizationRequestURL);
        assertTrue(StringUtils.isNotBlank(authorizationRequestURL));
        assertEquals(
                "https://agrirouter-qa.cfapps.eu10.hana.ondemand.com/application/905152eb-c526-47a3-b871-aa46d065bb4c/authorize?response_type=onboard&state=my-custom-state-to-identify-the-request&redirect_uri=",
                authorizationRequestURL);
    }

    @Test
    void givenValidParameterWhenCreatingTheUrlThenTheUrlShouldBeGeneratedForTelemetryPlatform() {
        AuthorizationRequestService authorizationRequestService =
                new AuthorizationRequestServiceImpl(new QA() {
                });
        AuthorizationRequestParameters authorizationRequestParameters =
                new AuthorizationRequestParameters();
        authorizationRequestParameters.setApplicationId(telemetryPlatform.getApplicationId());
        authorizationRequestParameters.setResponseType(SecuredOnboardingResponseType.ONBOARD);
        authorizationRequestParameters.setState("my-custom-state-to-identify-the-request");
        final String authorizationRequestURL =
                authorizationRequestService.getAuthorizationRequestURL(authorizationRequestParameters);
        assertNotNull(authorizationRequestURL);
        assertTrue(StringUtils.isNotBlank(authorizationRequestURL));
        assertEquals(
                "https://agrirouter-qa.cfapps.eu10.hana.ondemand.com/application/3c3559c9-7062-4628-a4f7-c9f5aa07265f/authorize?response_type=onboard&state=my-custom-state-to-identify-the-request&redirect_uri=",
                authorizationRequestURL);
    }

    @Test
    void givenInvalidParameterWhenCreatingTheUrlThenTheUrlShouldBeGenerated() {
        AuthorizationRequestService authorizationRequestService =
                new AuthorizationRequestServiceImpl(new QA() {
                });
        AuthorizationRequestParameters authorizationRequestParameters =
                new AuthorizationRequestParameters();
        authorizationRequestParameters.setResponseType(SecuredOnboardingResponseType.ONBOARD);
        Assertions.assertThrows(
                IllegalParameterDefinitionException.class,
                () ->
                        authorizationRequestService.getAuthorizationRequestURL(authorizationRequestParameters));
    }
}
