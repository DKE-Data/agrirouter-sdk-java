package com.dke.data.agrirouter.api.env;

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType;
import java.util.List;

/** Common Environment, holds some default methods pointing to the QA. */
public interface Environment {

  String AGRIROUTER_LOGIN_URL = "/app";

  String MQTT_URL_TEMPLATE = "ssl://%s:%s";
  String REGISTRATION_CODE_SERVICE_DATA_SERVICE_URL_TEMPLATE = "/application/%s/registrationcode";
  String AUTHORIZATION_SERVICE_DATA_SERVICE_URL_TEMPLATE = "/application/%s/authorize";
  String SECURED_ONBOARDING_AUTHORIZATION_LINK_TEMPLATE =
      "/application/%s/authorize?response_type=%s&state=%s&redirect_uri=%s";

  /**
   * Returning the AR base URL without trailing '/'.
   *
   * @return -
   */
  String getEnvironmentBaseUrl();

  /**
   * Returning the API prefix for several AR URLs, like the onboarding URL for example.
   *
   * @return -
   */
  String getApiPrefix();

  /**
   * URL for the registration service.
   *
   * @return -
   */
  String getRegistrationServiceUrl();

  /**
   * Client ID for the XSUAA-Token request.
   *
   * @return -
   */
  String getAgrirouterLoginUsername();

  /**
   * Client secret for the XSUAA-Token request.
   *
   * @return -
   */
  String getAgrirouterLoginPassword();

  /**
   * URL for the onboarding request.
   *
   * @return -
   */
  default String getOnboardUrl() {
    return getRegistrationServiceUrl() + getApiPrefix() + "/registration/onboard";
  }

  /**
   * URL for the secured onboarding request.
   *
   * @return -
   */
  default String getSecuredOnboardUrl() {
    return getRegistrationServiceUrl() + getApiPrefix() + "/registration/onboard/request";
  }

  /**
   * URL for verifying the secured onboarding request.
   *
   * @return -
   */
  default String getVerifyOnboardRequestUrl() {
    return getRegistrationServiceUrl() + getApiPrefix() + "/registration/onboard/verify";
  }

  /**
   * URL for revoking requests.
   *
   * @return -
   */
  default String getRevokeUrl() {
    return getRegistrationServiceUrl() + getApiPrefix() + "/registration/onboard/revoke";
  }

  /**
   * Returning the URL for the ui service to generate registration / TAN codes.
   *
   * @return -
   */
  default String getRegistrationServiceDataServiceUrl(String applicationId) {
    return this.getEnvironmentBaseUrl()
        + String.format(REGISTRATION_CODE_SERVICE_DATA_SERVICE_URL_TEMPLATE, applicationId);
  }

  /**
   * Returning the URL to login into the AR. This is necessary, because there are services within
   * the UI whiche are only avalailable if the user is logged in.
   *
   * @return -
   */
  default String getAgrirouterLoginUrl() {
    return this.getEnvironmentBaseUrl() + AGRIROUTER_LOGIN_URL;
  }

  /**
   * Returning the authorization URL for secured onboarding.
   *
   * @param responseType -
   * @param state -
   * @param redirectUrl -
   * @return -
   */
  default String getSecuredOnboardingAuthorizationUrl(
      String applicationId,
      SecuredOnboardingResponseType responseType,
      String state,
      String redirectUrl) {
    return this.getEnvironmentBaseUrl()
        + String.format(
            SECURED_ONBOARDING_AUTHORIZATION_LINK_TEMPLATE,
            applicationId,
            responseType.getKey(),
            state,
            redirectUrl);
  }

  /**
   * Returning the server URL for MQTT communication.
   *
   * @param host Host
   * @param port Port
   * @return -
   */
  default String getMqttServerUrl(String host, String port) {
    return String.format(MQTT_URL_TEMPLATE, host, port);
  }

  /**
   * All necessary root certificates to provide a secure connection.
   *
   * @return -
   */
  List<String> getRootCertificates();
}
