package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.registrationrequest.RegistrationRequestResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.onboard.RegistrationRequestService;
import com.dke.data.agrirouter.api.service.parameters.RegistrationRequestParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.CookieResolverService;
import com.dke.data.agrirouter.impl.validation.ResponseStatusChecker;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;

/** Internal service implementation. */
public class RegistrationRequestServiceImpl extends EnvironmentalService
    implements RegistrationRequestService, ResponseValidator {

  private final CookieResolverService cookieResolverService;

  public RegistrationRequestServiceImpl(Environment environment) {
    super(environment);
    this.cookieResolverService = new CookieResolverService(environment);
  }

  @Override
  public RegistrationRequestResponse getRegistrationCode(RegistrationRequestParameters parameters) {
    this.getNativeLogger()
        .info("BEGIN | Fetching registration code from agrirouter | '{}'.", parameters);

    parameters.validate();

    this.getNativeLogger().debug("Fetching cookies for current user.");
    Set<Cookie> cookies =
        this.cookieResolverService.cookies(
            this.environment.getAgrirouterLoginUsername(),
            this.environment.getAgrirouterLoginPassword());
    String url =
        this.environment.getRegistrationServiceDataServiceUrl(parameters.getApplicationId());

    this.getNativeLogger().debug("Fetching response for registration code request.");
    Response response = RequestFactory.request(url, cookies).get();

    this.getNativeLogger().debug("Validating response | {}.", response);
    if (!ResponseStatusChecker.isStatusInSuccessRange(response.getStatus())) {
      this.assertResponseStatusIsValid(response, HttpStatus.SC_OK);
    }

    this.getNativeLogger()
            .info("END | Fetching registration code from agrirouter | '{}'.", parameters);
    return response.readEntity(RegistrationRequestResponse.class);
  }
}
