package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.mqtt.HiveMqttClientWrapper;
import com.dke.data.agrirouter.api.mqtt.PahoMqttClientWrapper;
import com.dke.data.agrirouter.api.service.messaging.mqtt.MessageHeaderQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.mqtt.MessageQueryHelperService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class MessageHeaderQueryServiceImpl extends MqttService
        implements MessageHeaderQueryService, MessageSender {

    private final MessageQueryHelperService messageQueryHelperService;

    public MessageHeaderQueryServiceImpl(IMqttClient mqttClient) {
        super(new PahoMqttClientWrapper(mqttClient));
        messageQueryHelperService =
                new MessageQueryHelperService(
                        mqttClient, new EncodeMessageServiceImpl(), SystemMessageType.DKE_FEED_HEADER_QUERY);
    }

    public MessageHeaderQueryServiceImpl(Mqtt3AsyncClient mqttClient) {
        super(new HiveMqttClientWrapper(mqttClient));
        messageQueryHelperService =
                new MessageQueryHelperService(
                        mqttClient, new EncodeMessageServiceImpl(), SystemMessageType.DKE_FEED_HEADER_QUERY);
    }

    @Override
    public String send(MessageQueryParameters parameters) {
        return this.messageQueryHelperService.send(parameters);
    }

    @Override
    public MqttAsyncMessageSendingResult sendAsync(MessageQueryParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    @Override
    public FeedResponse.HeaderQueryResponse unsafeDecode(ByteString message)
            throws InvalidProtocolBufferException {
        return FeedResponse.HeaderQueryResponse.parseFrom(message);
    }

    @Override
    public String sendMessageToQueryAll(OnboardingResponse onboardingResponse) {
        return send(messageQueryHelperService.createMessageParametersToQueryAll(onboardingResponse));
    }

    @Override
    public MqttAsyncMessageSendingResult sendMessageToQueryAllAsync(
            OnboardingResponse onboardingResponse) {
        return sendAsync(
                messageQueryHelperService.createMessageParametersToQueryAll(onboardingResponse));
    }
}
