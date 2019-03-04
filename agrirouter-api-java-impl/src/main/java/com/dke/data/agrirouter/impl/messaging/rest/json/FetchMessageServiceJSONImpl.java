package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

public class FetchMessageServiceJSONImpl extends MessageFetcher implements FetchMessageService {

  int MAX_TRIES_BEFORE_FAILURE = 10;
  long DEFAULT_INTERVAL = 500;

  String EMPTY_CONTENT = "[]";

  private List<FetchMessageResponse> parseJson(String json) {
    Type type = new TypeToken<List<FetchMessageResponse>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  public FetchMessageResponse parseJson(byte[] json) {
    Type type = new TypeToken<FetchMessageResponse>() {}.getType();
    return new Gson().fromJson(new String(json), type);
  }

  public Optional<List<FetchMessageResponse>> poll(
      FetchMessageParameters parameters, int maxTries, long interval) {
    parameters.validate();
    int nrOfTries = 0;
    while (nrOfTries < maxTries) {
      Response response =
          RequestFactory.securedJSONRequest(
                  parameters.getOnboardingResponse().getConnectionCriteria().getCommands(),
                  parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                  parameters.getOnboardingResponse().getAuthentication().getSecret(),
                  CertificationType.valueOf(
                      parameters.getOnboardingResponse().getAuthentication().getType()))
              .get();
      this.assertStatusCodeIsOk(response.getStatus());
      String entityContent = response.readEntity(String.class);
      if (!StringUtils.equalsIgnoreCase(entityContent, EMPTY_CONTENT)) {
        Optional<String> result = Optional.of(entityContent);
        return result.map(this::parseJson);
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
