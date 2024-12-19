package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.mqtt.HiveMqttClientWrapper;
import com.dke.data.agrirouter.api.mqtt.PahoMqttClientWrapper;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Sending messages via MQTT using the given MQTT client. The service itself can not access the
 * queue because connection handling should not be part of the API.
 */
@SuppressWarnings("unused")
public class SendMessageServiceImpl extends MqttService
        implements SendMessageService<Void>, MessageBodyCreator {

    public SendMessageServiceImpl(IMqttClient mqttClient) {
        super(new PahoMqttClientWrapper(mqttClient));
    }

    public SendMessageServiceImpl(Mqtt3AsyncClient mqttClient) {
        super(new HiveMqttClientWrapper(mqttClient));
    }

    /**
     * Send message synchronous.
     *
     * @param sendMessageParameters Parameters to send the message.
     */
    public void send(SendMessageParameters sendMessageParameters) {
        sendMessageParameters.validate();
        var messageAsJson = this.createMessageBody(sendMessageParameters);
        var payload = messageAsJson.getBytes();
        this.getMqttClient()
                .publish(
                        Objects.requireNonNull(sendMessageParameters.getOnboardingResponse())
                                .getConnectionCriteria()
                                .getMeasures(),
                        payload);
    }

    @Override
    public CompletableFuture<Void> sendAsync(SendMessageParameters sendMessageParameters) {
        return CompletableFuture.runAsync(() -> this.send(sendMessageParameters));
    }
}
