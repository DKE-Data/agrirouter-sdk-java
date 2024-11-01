package com.dke.data.agrirouter.convenience.mqtt.hive;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.dto.onboard.RouterDevice;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotCreateMqttClientException;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Service to create a MQTT client using the given onboarding response.
 */
@SuppressWarnings("unused")
public class HiveMqttClientService extends EnvironmentalService {

    /**
     * Constructor for an environmental service.
     *
     * @param environment -
     */
    public HiveMqttClientService(Environment environment) {
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
    public Mqtt3AsyncClient create(OnboardingResponse onboardingResponse) {
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
    public Mqtt3AsyncClient create(RouterDevice routerDevice) {
        return this.createMqttClient(
                routerDevice.getConnectionCriteria().getHost(),
                String.valueOf(routerDevice.getConnectionCriteria().getPort()),
                routerDevice.getConnectionCriteria().getClientId());
    }

    private Mqtt3AsyncClient createMqttClient(String host, String port, String clientId) {
        if (StringUtils.isAnyBlank(host, port, clientId)) {
            throw new CouldNotCreateMqttClientException(
                    "Currently there are parameters missing. Did you onboard correctly - host, port or client id are missing.");
        }
        return MqttClient.builder()
                .useMqttVersion3()
                .identifier(Objects.requireNonNull(clientId))
                .serverHost(host)
                .serverPort(Integer.parseInt(port))
                .buildAsync();
    }
}
