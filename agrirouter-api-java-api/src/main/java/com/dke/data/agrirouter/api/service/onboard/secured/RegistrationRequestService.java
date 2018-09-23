package com.dke.data.agrirouter.api.service.onboard.secured;

import com.dke.data.agrirouter.api.dto.registrationrequest.secured.RegistrationRequestResponse;
import com.dke.data.agrirouter.api.dto.registrationrequest.secured.RegistrationRequestToken;
import com.dke.data.agrirouter.api.exception.*;
import com.dke.data.agrirouter.api.service.parameters.SecuredRegistrationRequestParameters;

/** Service for the registration request. */
public interface RegistrationRequestService {

  RegistrationRequestResponse getRegistrationCode(
      SecuredRegistrationRequestParameters securedRegistrationRequestParameters)
      throws InvalidUrlForRequestException, ForbiddenRequestException, UnauthorizedRequestException,
          UnexpectedHttpStatusException, CouldNotGetRegistrationCodeException,
          CouldNotFetchCookiesException;

  RegistrationRequestToken decode(String token);
}
