package com.dke.data.agrirouter.api.service.onboard;

import com.dke.data.agrirouter.api.dto.registrationrequest.RegistrationCodeResponse;
import com.dke.data.agrirouter.api.service.parameters.RegistrationCodeRequestParameters;

/** Service for the registration request. */
public interface RegistrationCodeRequestService {

  /**
   * Fetching a registration code from the registration service.
   *
   * @param parameters -
   * @return -
   */
  RegistrationCodeResponse getRegistrationCode(RegistrationCodeRequestParameters parameters);
}
