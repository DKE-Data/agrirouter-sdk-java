package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.mqtt.MessageHeaderQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.mqtt.MessageQueryHelperService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.concurrent.CompletableFuture;

import org.eclipse.paho.client.mqttv3.IMqttClient;

public class MessageHeaderQueryServiceImpl extends MqttService
        implements MessageHeaderQueryService, MessageSender {

    private final MessageQueryHelperService messageQueryHelperService;

    public MessageHeaderQueryServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
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
