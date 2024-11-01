package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.PingService;
import com.dke.data.agrirouter.api.service.parameters.PingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Collections;
import java.util.Objects;

/**
 * Service implementation.
 */
public class PingServiceImpl extends MqttService
        implements PingService, MessageBodyCreator, MessageEncoder {

    private final EncodeMessageService encodeMessageService;

    public PingServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(PingParameters parameters) {
        try {
            var encodedMessage = this.encode(parameters);
            var sendMessageParameters = new SendMessageParameters();
            sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
            sendMessageParameters.setEncodedMessages(
                    Collections.singletonList(encodedMessage.getEncodedMessage()));
            var messageAsJson = this.createMessageBody(sendMessageParameters);
            var payload = messageAsJson.getBytes();
            this.getMqttClient()
                    .publish(
                            Objects.requireNonNull(parameters.getOnboardingResponse())
                                    .getConnectionCriteria()
                                    .getMeasures(),
                            new MqttMessage(payload));
            return encodedMessage.getApplicationMessageID();
        } catch (MqttException e) {
            throw new CouldNotSendMqttMessageException(e);
        }
    }

    @Override
    public MqttAsyncMessageSendingResult sendAsync(PingParameters parameters) {
        throw new RuntimeException("Not implemented, please use the synchronous send method.");
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return encodeMessageService;
    }
}
