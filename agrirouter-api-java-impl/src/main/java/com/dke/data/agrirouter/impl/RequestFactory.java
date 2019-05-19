package com.dke.data.agrirouter.impl;

import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.exception.CertificationTypeNotSupportedException;
import com.dke.data.agrirouter.api.exception.CouldNotCreateDynamicKeyStoreException;
import com.dke.data.agrirouter.impl.common.ssl.KeyStoreCreationService;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.security.KeyStore;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

/** Factory to encapsulate the requests against the agrirouter */
public final class RequestFactory {

  /** Hidden constructor. */
  private RequestFactory() {
    // NOP
  }

  /**
   * Creating a request with SSL configuration using the PEM and KEY files from the agrirouter.
   *
   * @param url -
   * @param certificate -
   * @param password -
   * @return Builder -
   */
  public static Invocation.Builder securedRequest(
      String url, String certificate, String password, CertificationType certificationType) {
    ClientConfig clientConfig = new ClientConfig();
    KeyStore keyStore = createKeyStore(certificate, password, certificationType);
    Client client = createClient(clientConfig, keyStore, password, certificationType);
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
    WebTarget target = client.target(url);
    Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
    request.accept(MediaType.APPLICATION_JSON_TYPE);
    return request;
  }

  private static Client createClient(
      ClientConfig clientConfig,
      KeyStore keyStore,
      String password,
      CertificationType certificationType) {
    try {
      switch (certificationType) {
        case PEM:
          return ClientBuilder.newBuilder()
              .withConfig(clientConfig)
              .keyStore(keyStore, KeyStoreCreationService.TEMPORARY_KEY_PASSWORD)
              .build();
        case P12:
          return ClientBuilder.newBuilder()
              .withConfig(clientConfig)
              .keyStore(keyStore, password)
              .build();
        default:
          throw new CertificationTypeNotSupportedException(certificationType);
      }
    } catch (Exception e) {
      throw new CouldNotCreateDynamicKeyStoreException(e);
    }
  }

  private static KeyStore createKeyStore(
      String x509Certificate, String password, CertificationType certificationType) {
    KeyStoreCreationService keyStoreCreationService = new KeyStoreCreationService();
    try {
      switch (certificationType) {
        case PEM:
          return keyStoreCreationService.createAndReturnKeystoreFromPEM(x509Certificate, password);
        case P12:
          return keyStoreCreationService.createAndReturnKeystoreFromP12(x509Certificate, password);
        default:
          throw new CertificationTypeNotSupportedException(certificationType);
      }
    } catch (Exception e) {
      throw new CouldNotCreateDynamicKeyStoreException(e);
    }
  }

  /**
   * Setting the 'reg_access_token' within the header.
   *
   * @param url -
   * @param accessToken -
   * @return Builder -
   */
  public static Invocation.Builder bearerTokenRequest(String url, String accessToken) {
    Client client = ClientBuilder.newClient();
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
    WebTarget target = client.target(url);
    Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
    request.accept(MediaType.APPLICATION_JSON_TYPE);
    request.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

    return request;
  }

  /**
   * Setting applicationID and Signature within the header.
   *
   * @param url -
   * @param applicationId -
   * @param signature -
   * @return Builder -
   */
  public static Invocation.Builder signedDeleteRequest(
      String url, String applicationId, String signature) {
    Client client = ClientBuilder.newClient();
    client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
    WebTarget target = client.target(url);
    Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
    request.accept(MediaType.APPLICATION_JSON_TYPE);
    request.header(AgrirouterHttpHeader.APPLICATION_ID, applicationId);
    request.header(AgrirouterHttpHeader.SIGNATURE, signature);

    return request;
  }

  /**
   * Setting the 'reg_access_token' within the header.
   *
   * @param url -
   * @param accessToken -
   * @return Builder -
   */
  public static Invocation.Builder bearerTokenRequest(
      String url, String accessToken, String applicationId, String signature) {
    Client client = ClientBuilder.newClient();
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
    WebTarget target = client.target(url);
    Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
    request.accept(MediaType.APPLICATION_JSON_TYPE);
    request.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    request.header(AgrirouterHttpHeader.APPLICATION_ID, applicationId);
    request.header(AgrirouterHttpHeader.SIGNATURE, signature);
    return request;
  }

  public class AgrirouterHttpHeader {
    public static final String APPLICATION_ID = "X-Agrirouter-ApplicationId";
    public static final String SIGNATURE = "X-Agrirouter-Signature";
  }

  /**
   * Setting the cookies for the request.
   *
   * @param url -
   * @return Builder -
   */
  public static Invocation.Builder request(String url, Set<Cookie> cookies) {
    Client client = ClientBuilder.newClient();
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
    WebTarget target = client.target(url);
    Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
    request.accept(MediaType.APPLICATION_JSON_TYPE);
    cookies
        .stream()
        .map(
            cookie ->
                new javax.ws.rs.core.Cookie(
                    cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain()))
        .forEach(request::cookie);
    return request;
  }
}
