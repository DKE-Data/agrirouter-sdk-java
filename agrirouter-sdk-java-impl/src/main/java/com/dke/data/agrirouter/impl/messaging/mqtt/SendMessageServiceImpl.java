package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Sending messages via MQTT using the given MQTT client. The service itself can not access the
 * queue because connection handling should not be part of the API.
 */
public class SendMessageServiceImpl extends MqttService
        implements SendMessageService<Void>, MessageBodyCreator {

    public SendMessageServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
    }

    /**
     * Send message synchronous.
     *
     * @param sendMessageParameters Parameters to send the message.
     */
    public void send(SendMessageParameters sendMessageParameters) {
        sendMessageParameters.validate();
        try {
            String messageAsJson = this.createMessageBody(sendMessageParameters);
            byte[] payload = messageAsJson.getBytes();
            this.getMqttClient()
                    .publish(
                            Objects.requireNonNull(sendMessageParameters.getOnboardingResponse())
                                    .getConnectionCriteria()
                                    .getMeasures(),
                            new MqttMessage(payload));
        } catch (MqttException e) {
            throw new CouldNotSendMqttMessageException(e);
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(SendMessageParameters sendMessageParameters) {
        return CompletableFuture.runAsync(() -> this.send(sendMessageParameters));
    }
}
