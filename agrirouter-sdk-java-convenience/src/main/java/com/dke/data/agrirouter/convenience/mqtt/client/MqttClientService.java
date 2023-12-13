package com.dke.data.agrirouter.convenience.mqtt.client;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.RouterDevice;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotCreateMqttClientException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Objects;

/**
 * Service to create a MQTT client using the given onboarding response.
 */
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
        return this.createMqttClient(
                onboardingResponse.getConnectionCriteria().getHost(),
                onboardingResponse.getConnectionCriteria().getPort(),
                onboardingResponse.getConnectionCriteria().getClientId());
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
        return this.createMqttClient(
                routerDevice.getConnectionCriteria().getHost(),
                String.valueOf(routerDevice.getConnectionCriteria().getPort()),
                routerDevice.getDeviceAlternateId());
    }

    private IMqttClient createMqttClient(String host, String port, String clientId) {
        try {
            if (StringUtils.isAnyBlank(host, port, clientId)) {
                throw new CouldNotCreateMqttClientException(
                        "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
            }
            return new MqttClient(
                    this.environment.getMqttServerUrl(host, port), Objects.requireNonNull(clientId));

        } catch (MqttException e) {
            throw new CouldNotCreateMqttClientException("Could not create MQTT client.", e);
        }
    }
}
