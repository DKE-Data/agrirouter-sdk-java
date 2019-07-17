package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.RouterDevice;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotCreateMqttClientException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttClientService extends EnvironmentalService {

  public MqttClientService(Environment environment) {
    super(environment);
  }

  public IMqttClient create(OnboardingResponse onboardingResponse) {
    try {
      if (StringUtils.isAnyBlank(
          onboardingResponse.getConnectionCriteria().getHost(),
          onboardingResponse.getConnectionCriteria().getPort(),
          onboardingResponse.getConnectionCriteria().getClientId())) {
        throw new CouldNotCreateMqttClientException(
            "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
      }
      return new MqttClient(
          this.environment.getMqttServerUrl(
              onboardingResponse.getConnectionCriteria().getHost(),
              onboardingResponse.getConnectionCriteria().getPort()),
          onboardingResponse.getConnectionCriteria().getClientId());
    } catch (MqttException e) {
      throw new CouldNotCreateMqttClientException("Could not create MQTT client.", e);
    }
  }

  public IMqttClient create(RouterDevice routerDevice) {
    try {
      if (StringUtils.isAnyBlank(
          routerDevice.getConnectionCriteria().getHost(),
          String.valueOf(routerDevice.getConnectionCriteria().getPort()),
          routerDevice.getConnectionCriteria().getClientId())) {
        throw new CouldNotCreateMqttClientException(
            "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
      }
      return new MqttClient(
          this.environment.getMqttServerUrl(
              routerDevice.getConnectionCriteria().getHost(),
              String.valueOf(routerDevice.getConnectionCriteria().getPort())),
          routerDevice.getConnectionCriteria().getClientId());
    } catch (MqttException e) {
      throw new CouldNotCreateMqttClientException("Could not create MQTT client.", e);
    }
  }

  public void subscribeEndpoint(IMqttClient mqttClient, OnboardingResponse onboardingResponse)
      throws MqttException {
    mqttClient.subscribe(onboardingResponse.getConnectionCriteria().getCommands());
  }
}
