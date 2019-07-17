package com.dke.data.agrirouter.convenience.mqtt.client;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.ssl.KeyStoreCreationService;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/** Creating MQTT options for the MQTT client. */
public class MqttOptionService extends EnvironmentalService {

  private final KeyStoreCreationService keyStoreCreationService = new KeyStoreCreationService();

  /**
   * Constructor for an environmental service.
   *
   * @param environment -
   */
  public MqttOptionService(Environment environment) {
    super(environment);
  }

  /**
   * Create default MQTT connect options using the given onboarding response. The MQTT options
   * contain a socket factory which uses the personal certificate of the endpoint.
   *
   * @param onboardingResponse -
   * @return -
   * @throws Exception -
   */
  public MqttConnectOptions createMqttConnectOptions(OnboardingResponse onboardingResponse)
      throws Exception {
    MqttConnectOptions options = new MqttConnectOptions();
    CertificationType certificationType =
        CertificationType.valueOf(onboardingResponse.getAuthentication().getType());
    options.setSocketFactory(
        this.getSocketFactory(
            onboardingResponse.getAuthentication().getCertificate(),
            onboardingResponse.getAuthentication().getSecret(),
            certificationType));
    options.setKeepAliveInterval(60);
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    return options;
  }

  /**
   * Creating the socket factory for the MQTT options.
   *
   * @param certificate Certificate of the endpoint.
   * @param password The password for the certificate.
   * @param certificationType The type of certificate.
   * @return -
   * @throws Exception -
   */
  private SocketFactory getSocketFactory(
      String certificate, String password, CertificationType certificationType) throws Exception {
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    if (certificationType == CertificationType.PEM) {
      kmf.init(
          this.keyStoreCreationService.createAndReturnKeystoreFromPEM(certificate, password),
          "changeit".toCharArray());
    } else {
      if (certificationType == CertificationType.P12) {
        kmf.init(
            this.keyStoreCreationService.createAndReturnKeystoreFromP12(certificate, password),
            password.toCharArray());
      }
    }
    SSLContext context = SSLContext.getInstance("TLSv1.2");
    context.init(kmf.getKeyManagers(), null, null);
    return context.getSocketFactory();
  }
}
