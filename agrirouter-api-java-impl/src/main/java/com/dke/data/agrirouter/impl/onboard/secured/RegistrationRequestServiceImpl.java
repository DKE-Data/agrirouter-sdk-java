package com.dke.data.agrirouter.impl.onboard.secured;

import com.dke.data.agrirouter.api.dto.registrationrequest.secured.RegistrationRequestResponse;
import com.dke.data.agrirouter.api.dto.registrationrequest.secured.RegistrationRequestToken;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotGetRegistrationCodeException;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.onboard.secured.RegistrationRequestService;
import com.dke.data.agrirouter.api.service.parameters.AuthenticationUrlParameters;
import com.dke.data.agrirouter.api.service.parameters.SecuredRegistrationRequestParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.CookieResolverService;
import com.dke.data.agrirouter.impl.common.StateIdService;
import com.dke.data.agrirouter.impl.onboard.OnboardingServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

/** Internal service implementation. */
public class RegistrationRequestServiceImpl extends EnvironmentalService
    implements RegistrationRequestService, ResponseValidator {

  private static final String SIGNATURE_KEY = "signature";
  private static final String STATE_KEY = "state";
  private static final String TOKEN_KEY = "token";
  private static final String ERROR_KEY = "error";

  private CookieResolverService cookieResolverService;
  private final OnboardingService onboardingService;

  public RegistrationRequestServiceImpl(Environment environment) {
    super(environment);
    this.onboardingService = new OnboardingServiceImpl(environment);
  }

  /**
   * This function returns a full URL to send to a browser as Redirect or Link to forward a user to
   * the Authorization process
   *
   * @param securedRegistrationRequestParameters Parameters to build URL from
   * @return The RegistrationURL
   */
  public String getRegistrationRequestURL(
      SecuredRegistrationRequestParameters securedRegistrationRequestParameters) {

    AuthenticationUrlParameters authenticationUrlParameters = new AuthenticationUrlParameters();
    authenticationUrlParameters.setApplicationId(
        securedRegistrationRequestParameters.getApplicationId());
    authenticationUrlParameters.setRedirectUri(
        securedRegistrationRequestParameters.getRedirectUri());
    authenticationUrlParameters.setResponseType(
        securedRegistrationRequestParameters.getResponseType());
    if (StringUtils.isBlank(securedRegistrationRequestParameters.getState())) {
      securedRegistrationRequestParameters.setState(StateIdService.generateState());
    }
    authenticationUrlParameters.setState(securedRegistrationRequestParameters.getState());

    return this.onboardingService.generateAuthenticationUrl(authenticationUrlParameters);
  }

  /**
   * This function creates a full request to do the authorization without user interaction. This
   * function is usable for automated testing, it should NOT be used for final implementations!
   *
   * @param securedRegistrationRequestParameters Parameters to build URL from
   * @return The RegistrationURL
   */
  @Override
  public RegistrationRequestResponse getRegistrationCode(
      SecuredRegistrationRequestParameters securedRegistrationRequestParameters) {
    securedRegistrationRequestParameters.validate();

    if (this.cookieResolverService == null) {
      this.cookieResolverService = new CookieResolverService(environment);
    }

    Set<Cookie> cookies =
        this.cookieResolverService.cookies(
            this.environment.getAgrirouterLoginUsername(),
            this.environment.getAgrirouterLoginPassword());

    try (final WebClient webClient = new WebClient()) {
      webClient.setAjaxController(new NicelyResynchronizingAjaxController());
      webClient.getOptions().setThrowExceptionOnScriptError(false);
      webClient.getOptions().setUseInsecureSSL(true);

      cookies.forEach(c -> webClient.getCookieManager().addCookie(c));

      final String url = getRegistrationRequestURL(securedRegistrationRequestParameters);

      final HtmlPage page = webClient.getPage(url);

      HtmlAnchor anchorByHref = page.getAnchorByHref("javascript:{}");
      final Page redirectPage = anchorByHref.click();
      assertResponseStatusIsValid(redirectPage.getWebResponse(), HttpStatus.SC_OK);

      URL redirectPageUrl = redirectPage.getUrl();
      return this.extractAuthenticationResults(redirectPageUrl);
    } catch (IOException e) {
      throw new CouldNotGetRegistrationCodeException(e);
    } catch (FailingHttpStatusCodeException e) {
      throw new CouldNotGetRegistrationCodeException(
          "The provided application id was not valid.", e);
    }
  }

  @Override
  public RegistrationRequestToken decodeToken(String token) {
    byte[] decodedBytes = Base64.getDecoder().decode(token);
    String decodedToken = new String(decodedBytes);
    return new Gson().fromJson(decodedToken, RegistrationRequestToken.class);
  }

  @NotNull
  public RegistrationRequestResponse extractAuthenticationResults(URL redirectPageUrl) {
    String[] queryParams = redirectPageUrl.getQuery().split("&");
    Map<String, String> authenticationResults = new HashMap<>();
    Arrays.stream(queryParams)
        .forEach(
            s -> {
              String[] keyValuePair = s.split("=");
              try {
                authenticationResults.put(
                    keyValuePair[0], URLDecoder.decode(keyValuePair[1], "UTF-8"));
              } catch (UnsupportedEncodingException e) {
                // NOP
              }
            });
    RegistrationRequestResponse registrationRequestResponse = new RegistrationRequestResponse();
    registrationRequestResponse.setSignature(authenticationResults.get(SIGNATURE_KEY));
    registrationRequestResponse.setState(authenticationResults.get(STATE_KEY));
    registrationRequestResponse.setToken(authenticationResults.get(TOKEN_KEY));
    registrationRequestResponse.setError(authenticationResults.get(ERROR_KEY));
    return registrationRequestResponse;
  }

  public RegistrationRequestResponse extractAuthenticationResults(String redirectPageUrl)
      throws MalformedURLException {
    URL url = new URL(redirectPageUrl);

    return extractAuthenticationResults(url);
  }
}
