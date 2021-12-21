package com.dke.data.agrirouter.api.env;

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType;

/** Common Environment, holds some default methods pointing to the QA. */
public interface Environment {

  /** Template for MQTT connections. */
  String MQTT_URL_TEMPLATE = "ssl://%s:%s";

  /** Link template for the secured onboarding process. */
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
}
