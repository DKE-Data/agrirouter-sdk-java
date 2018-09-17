package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotCreateMqttClientException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttClientService extends EnvironmentalService {

  public MqttClientService(Environment environment) {
    super(environment);
  }

  public IMqttClient create(OnboardingResponse onboardingResponse) {
    try {
      return new MqttClient(
          this.environment.getMqttServerUrl(
              onboardingResponse.getConnectionCriteria().getHost(),
              onboardingResponse.getConnectionCriteria().getPort()),
          onboardingResponse.getConnectionCriteria().getClientId());
    } catch (MqttException e) {
      throw new CouldNotCreateMqttClientException("Could not create MQTT client.", e);
    }
  }
}
