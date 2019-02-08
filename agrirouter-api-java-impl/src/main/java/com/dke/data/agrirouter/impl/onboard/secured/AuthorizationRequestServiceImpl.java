package com.dke.data.agrirouter.impl.onboard.secured;

import com.dke.data.agrirouter.api.dto.registrationrequest.secured.AuthorizationResponse;
import com.dke.data.agrirouter.api.dto.registrationrequest.secured.AuthorizationResponseToken;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotGetRegistrationCodeException;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.onboard.secured.AuthorizationRequestService;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
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
import org.jetbrains.annotations.NotNull;

/** Internal service implementation. */
public class AuthorizationRequestServiceImpl extends EnvironmentalService
    implements AuthorizationRequestService, ResponseValidator {

  private static final String SIGNATURE_KEY = "signature";
  private static final String STATE_KEY = "state";
  private static final String TOKEN_KEY = "token";
  private static final String ERROR_KEY = "error";

  private CookieResolverService cookieResolverService;
  private final OnboardingService onboardingService;

  public AuthorizationRequestServiceImpl(Environment environment) {
    super(environment);
    this.onboardingService = new OnboardingServiceImpl(environment);
  }

  /**
   * This function returns a full URL to send to a browser as Redirect or Link to forward a user to
   * the Authorization process
   *
   * @param authorizationRequestParameters Parameters to build URL from
   * @return The RegistrationURL
   */
  public String getAuthorizationRequestURL(
      AuthorizationRequestParameters authorizationRequestParameters) {
    if (StringUtils.isBlank(authorizationRequestParameters.getState())) {
      authorizationRequestParameters.setState(StateIdService.generateState());
    }
    authorizationRequestParameters.setState(authorizationRequestParameters.getState());

    return this.onboardingService.generateAuthorizationUrl(authorizationRequestParameters);
  }

  /**
   * This function creates a full request to do the authorization without user interaction. This
   * function is usable for automated testing, it should NOT be used for final implementations!
   *
   * @param authorizationRequestParameters Parameters to build URL from
   * @return The RegistrationURL
   */
  @Override
  public AuthorizationResponse callForAuthorizationResponse(
      AuthorizationRequestParameters authorizationRequestParameters) {
    authorizationRequestParameters.validate();

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

      final String url = getAuthorizationRequestURL(authorizationRequestParameters);

      final HtmlPage page = webClient.getPage(url);

      HtmlAnchor anchorByHref = page.getAnchorByHref("javascript:{}");
      final Page redirectPage = anchorByHref.click();
      this.assertStatusCodeIsOk(redirectPage.getWebResponse().getStatusCode());

      URL redirectPageUrl = redirectPage.getUrl();
      return this.extractAuthorizationResponse(redirectPageUrl);
    } catch (IOException e) {
      throw new CouldNotGetRegistrationCodeException(e);
    } catch (FailingHttpStatusCodeException e) {
      throw new CouldNotGetRegistrationCodeException(
          "The provided application id was not valid.", e);
    }
  }

  /**
   * Decode the Base64-encoded Token and Create a TokenObject with RegCode and AccountId
   *
   * @param token
   * @return
   */
  @Override
  public AuthorizationResponseToken decodeToken(String token) {
    byte[] decodedBytes = Base64.getDecoder().decode(token);
    String decodedToken = new String(decodedBytes);
    return new Gson().fromJson(decodedToken, AuthorizationResponseToken.class);
  }

  @NotNull
  public AuthorizationResponse extractAuthorizationResponseFromQuery(String query) {
    String[] queryParams = query.split("&");
    Map<String, String> authorizationResults = new HashMap<>();
    Arrays.stream(queryParams)
        .forEach(
            s -> {
              String[] keyValuePair = s.split("=");
              try {
                authorizationResults.put(
                    keyValuePair[0], URLDecoder.decode(keyValuePair[1], "UTF-8"));
              } catch (UnsupportedEncodingException e) {
                // NOP
              }
            });
    AuthorizationResponse authorizationResponse = new AuthorizationResponse();
    authorizationResponse.setSignature(authorizationResults.get(SIGNATURE_KEY));
    authorizationResponse.setState(authorizationResults.get(STATE_KEY));
    authorizationResponse.setToken(authorizationResults.get(TOKEN_KEY));
    authorizationResponse.setError(authorizationResults.get(ERROR_KEY));
    return authorizationResponse;
  }

  @NotNull
  public AuthorizationResponse extractAuthorizationResponse(URL redirectPageUrl) {
    return extractAuthorizationResponseFromQuery(redirectPageUrl.getQuery());
  }

  public AuthorizationResponse extractAuthorizationResults(String redirectPageUrl)
      throws MalformedURLException {
    URL url = new URL(redirectPageUrl);

    return extractAuthorizationResponse(url);
  }
}
