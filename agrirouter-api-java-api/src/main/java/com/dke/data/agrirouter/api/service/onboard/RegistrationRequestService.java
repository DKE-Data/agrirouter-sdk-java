package com.dke.data.agrirouter.api.service.onboard;

import com.dke.data.agrirouter.api.dto.registrationrequest.RegistrationRequestResponse;
import com.dke.data.agrirouter.api.service.parameters.RegistrationRequestParameters;

/** Service for the registration request. */
public interface RegistrationRequestService {

  /**
   * Fetching a registration code from the registration service.
   *
   * @param parameters -
   * @return -
   */
  RegistrationRequestResponse getRegistrationCode(RegistrationRequestParameters parameters);
}
