package com.dke.data.agrirouter.documentation;

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.impl.onboard.secured.AuthorizationRequestServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CreateAuthorizationUrlForSecuredOnboardingProcessTest extends AbstractIntegrationTest {

    @Test
    void givenValidParametersWhenCreatingTheUrlThenTheUrlShouldBeGenerated() {
        var authorizationRequestService = new AuthorizationRequestServiceImpl(farmingSoftware.getEnvironment());

        // [0] Define a custom state to identify the request, this is part of the application itself.ß
        var state = UUID.randomUUID().toString();

        // [1] Define the parameters for the authorization request
        // ============================================================
        var parameters = new AuthorizationRequestParameters();
        parameters.setApplicationId("REPLACE_ME_WITH_ACTUAL_APPLICATION_ID");
        parameters.setResponseType(SecuredOnboardingResponseType.ONBOARD);
        parameters.setState(state);

        // https://app.qa.agrirouter.farm/application/REPLACE_ME_WITH_ACTUAL_APPLICATION_ID/authorize?response_type=onboard&state=5dbbe1de-4e30-40fa-acd7-a17f9e3d6602&redirect_uri=
        var authorizationRequestURL = authorizationRequestService.getAuthorizationRequestURL(parameters);

        // [2] Validate the generated URL
        // ============================================================
        Assertions.assertNotNull(authorizationRequestURL);
        Assertions.assertTrue(authorizationRequestURL.startsWith("https://app.qa.agrirouter.farm/application/"));
        Assertions.assertTrue(authorizationRequestURL.contains("/REPLACE_ME_WITH_ACTUAL_APPLICATION_ID/"));
        Assertions.assertTrue(authorizationRequestURL.contains("/authorize?response_type=onboard&state=" + state));
        Assertions.assertTrue(authorizationRequestURL.endsWith("&redirect_uri="));
    }

}
