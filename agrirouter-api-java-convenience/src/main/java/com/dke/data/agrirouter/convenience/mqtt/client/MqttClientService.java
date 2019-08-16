package com.dke.data.agrirouter.convenience.mqtt.client;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.RouterDevice;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotCreateMqttClientException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/** Service to create a MQTT client using the given onboarding response. */
public class MqttClientService extends EnvironmentalService {

  /**
   * Constructor for an environmental service.
   *
   * @param environment -
   */
  public MqttClientService(Environment environment) {
    super(environment);
  }

  /**
   * Creates a MQTT client using the given onboarding response. Communication relies on given root
   * certificates in an external keystore. The keystore with the root certificates is not created
   * locally.
   *
   * @param onboardingResponse -
   * @return -
   */
  public IMqttClient create(OnboardingResponse onboardingResponse) {
    try {
      if (StringUtils.isAnyBlank(
          onboardingResponse.getConnectionCriteria().getHost(),
          onboardingResponse.getConnectionCriteria().getPort(),
          onboardingResponse.getConnectionCriteria().getClientId())) {
        throw new CouldNotCreateMqttClientException(
            "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
      } else {
        return new MqttClient(
            this.environment.getMqttServerUrl(
                onboardingResponse.getConnectionCriteria().getHost(),
                onboardingResponse.getConnectionCriteria().getPort()),
            Objects.requireNonNull(onboardingResponse.getConnectionCriteria().getClientId()));
      }
    } catch (MqttException var3) {
      throw new CouldNotCreateMqttClientException("Could not create MQTT client.", var3);
    }
  }

  /**
   * Creates a MQTT client using the given router Device. Communication relies on given root
   * certificates in an external keystore. The keystore with the root certificates is not created
   * locally.
   *
   * @param routerDevice -
   * @return -
   */
  public IMqttClient create(RouterDevice routerDevice) {
    try {
      if (StringUtils.isAnyBlank(
          routerDevice.getConnectionCriteria().getHost(),
          String.valueOf(routerDevice.getConnectionCriteria().getPort()),
          routerDevice.getDeviceAlternateId())) {
        throw new CouldNotCreateMqttClientException(
            "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
      } else {
        return new MqttClient(
            this.environment.getMqttServerUrl(
                routerDevice.getConnectionCriteria().getHost(),
                String.valueOf(routerDevice.getConnectionCriteria().getPort())),
            Objects.requireNonNull(routerDevice.getDeviceAlternateId()));
      }
    } catch (MqttException var3) {
      throw new CouldNotCreateMqttClientException("Could not create MQTT client.", var3);
    }
  }
}
