package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.DeleteMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.DeleteAllMessagesParameterCreator;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class DeleteMessageServiceImpl extends MqttService
        implements DeleteMessageService,
        MessageBodyCreator,
        MessageEncoder,
        DeleteAllMessagesParameterCreator {

    private final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    public DeleteMessageServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
    }

    @Override
    public String send(DeleteMessageParameters parameters) {
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
    public MqttAsyncMessageSendingResult sendAsync(DeleteMessageParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    public EncodeMessageService getEncodeMessageService() {
        return encodeMessageService;
    }

    @Override
    public String sendMessageToDeleteAll(OnboardingResponse onboardingResponse) {
        final var deleteMessageParameters =
                createMessageParametersToDeleteAllMessages(onboardingResponse);
        return send(deleteMessageParameters);
    }

    @Override
    public MqttAsyncMessageSendingResult sendMessageToDeleteAllAsync(
            OnboardingResponse onboardingResponse) {
        final var deleteMessageParameters =
                createMessageParametersToDeleteAllMessages(onboardingResponse);
        return sendAsync(deleteMessageParameters);
    }
}
