package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.mqtt.PahoMqttClientWrapper;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.SetSubscriptionService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SetSubscriptionParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class SetSubscriptionServiceImpl extends MqttService
        implements SetSubscriptionService, MessageBodyCreator, MessageEncoder, ResponseValidator {
    private final EncodeMessageService encodeMessageService;

    public SetSubscriptionServiceImpl(IMqttClient mqttClient) {
        super(new PahoMqttClientWrapper(mqttClient));
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(SetSubscriptionParameters parameters) {
        parameters.validate();
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
                        payload);
        return encodedMessage.getApplicationMessageID();
    }

    @Override
    public MqttAsyncMessageSendingResult sendAsync(SetSubscriptionParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return encodeMessageService;
    }
}
