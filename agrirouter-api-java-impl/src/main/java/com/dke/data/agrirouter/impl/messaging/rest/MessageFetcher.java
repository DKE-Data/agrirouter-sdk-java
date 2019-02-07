package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

public interface MessageFetcher extends ResponseValidator {

  int MAX_TRIES_BEFORE_FAILURE = 10;
  long DEFAULT_INTERVAL = 500;

  String EMPTY_CONTENT = "[]";

  default Optional<String> poll(FetchMessageParameters parameters, int maxTries, long interval) {
    parameters.validate();
    int nrOfTries = 0;
    while (nrOfTries < maxTries) {
      Response response =
          RequestFactory.securedRequest(
                  parameters.getOnboardingResponse().getConnectionCriteria().getCommands(),
                  parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                  parameters.getOnboardingResponse().getAuthentication().getSecret(),
                  CertificationType.valueOf(
                      parameters.getOnboardingResponse().getAuthentication().getType()))
              .get();
      this.assertStatusCodeIsOk(response.getStatus());
      String entityContent = response.readEntity(String.class);
      if (!StringUtils.equalsIgnoreCase(entityContent, EMPTY_CONTENT)) {
        return Optional.of(entityContent);
      }
      nrOfTries++;
      try {
        Thread.sleep(interval);
      } catch (InterruptedException nop) {
        // NOP
      }
    }
    return Optional.empty();
  }
}
