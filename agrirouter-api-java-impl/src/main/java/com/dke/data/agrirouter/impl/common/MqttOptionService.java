package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.RouterDevice;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.ssl.KeyStoreCreationService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttOptionService extends EnvironmentalService {

  private final KeyStoreCreationService keyStoreCreationService;

  public MqttOptionService(Environment environment) {
    super(environment);
    this.keyStoreCreationService = new KeyStoreCreationService();
  }

  public MqttConnectOptions createMqttConnectOptions(OnboardingResponse onboardingResponse)
      throws Exception {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setSocketFactory(
        this.keyStoreCreationService.getSocketFactory(
            this.environment.getRootCertificates(),
            onboardingResponse.getAuthentication().getCertificate(),
            onboardingResponse.getAuthentication().getSecret()));
    options.setKeepAliveInterval(60);
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    return options;
  }

  public MqttConnectOptions createMqttConnectOptions(RouterDevice routerDevice) throws Exception {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setSocketFactory(
        this.keyStoreCreationService.getSocketFactory(
            this.environment.getRootCertificates(),
            routerDevice.getAuthentication().getCertificate(),
            routerDevice.getAuthentication().getSecret()));
    options.setKeepAliveInterval(60);
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    return options;
  }
}
