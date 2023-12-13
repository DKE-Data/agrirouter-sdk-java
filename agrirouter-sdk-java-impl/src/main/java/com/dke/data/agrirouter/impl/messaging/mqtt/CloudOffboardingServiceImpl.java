package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.CloudOffboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Service implementation.
 */
@SuppressWarnings("unused")
public class CloudOffboardingServiceImpl extends MqttService
        implements CloudOffboardingService, MessageBodyCreator, MessageEncoder {

    private final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    public CloudOffboardingServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
    }

    /**
     * Offboarding a virtual CU. Will deliver no result if the action was successful, if there's any
     * error an exception will be thrown.
     *
     * @param parameters Parameters for offboarding.
     */
    @Override
    public String send(CloudOffboardingParameters parameters) {
        parameters.validate();
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
    public MqttAsyncMessageSendingResult sendAsync(CloudOffboardingParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
