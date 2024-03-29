package com.dke.data.agrirouter.api.service.onboard.secured;

import com.dke.data.agrirouter.api.dto.registrationrequest.secured.AuthorizationResponse;
import com.dke.data.agrirouter.api.dto.registrationrequest.secured.AuthorizationResponseToken;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Service for the registration request.
 */
@SuppressWarnings("unused")
public interface AuthorizationRequestService {

    String getAuthorizationRequestURL(AuthorizationRequestParameters authorizationRequestParameters);

    AuthorizationResponse extractAuthorizationResponseFromQuery(String query);

    AuthorizationResponse extractAuthorizationResponse(URL redirectPageUrl);

    AuthorizationResponse extractAuthorizationResults(String redirectPageUrl)
            throws MalformedURLException;

    AuthorizationResponseToken decodeToken(String token);
}
