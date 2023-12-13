package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.CloudOnboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
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
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class CloudOnboardingServiceImpl extends MqttService
        implements CloudOnboardingService, MessageBodyCreator, MessageEncoder {

    private final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    public CloudOnboardingServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
    }

    /**
     * Onboarding a virtual CU for an existing cloud application (incl. several checks).
     *
     * @param parameters Parameters for the onboarding.
     * @return -
     */
    @Override
    public String send(CloudOnboardingParameters parameters) {
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
    public MqttAsyncMessageSendingResult sendAsync(CloudOnboardingParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
