package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.mqtt.HiveMqttClientWrapper;
import com.dke.data.agrirouter.api.mqtt.PahoMqttClientWrapper;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.messaging.mqtt.ListEndpointsService;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class ListEndpointsServiceImpl extends MqttService
        implements ListEndpointsService,
        MessageBodyCreator,
        MessageEncoder,
        MessageDecoder<agrirouter.response.payload.account.Endpoints.ListEndpointsResponse> {

    private final EncodeMessageService encodeMessageService;

    public ListEndpointsServiceImpl(IMqttClient mqttClient) {
        super(new PahoMqttClientWrapper(mqttClient));
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    public ListEndpointsServiceImpl(Mqtt3AsyncClient mqttClient) {
        super(new HiveMqttClientWrapper(mqttClient));
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(ListEndpointsParameters parameters) {
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
    public MqttAsyncMessageSendingResult sendAsync(ListEndpointsParameters parameters) {
        return new MqttAsyncMessageSendingResult(
                CompletableFuture.supplyAsync(() -> this.send(parameters)));
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }

    @Override
    public String sendMessageToListAllWithExistingRoute(OnboardingResponse onboardingResponse) {
        var listEndpointsParameters = new ListEndpointsParameters();
        listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
        listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
        listEndpointsParameters.setOnboardingResponse(onboardingResponse);
        listEndpointsParameters.setUnfilteredList(false);
        return send(listEndpointsParameters);
    }

    @Override
    public String sendMessageToListAll(OnboardingResponse onboardingResponse) {
        var listEndpointsParameters = new ListEndpointsParameters();
        listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
        listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
        listEndpointsParameters.setOnboardingResponse(onboardingResponse);
        listEndpointsParameters.setUnfilteredList(true);
        return send(listEndpointsParameters);
    }

    @Override
    public MqttAsyncMessageSendingResult sendMessageToListAllWithExistingRouteAsync(
            OnboardingResponse onboardingResponse) {
        var listEndpointsParameters = new ListEndpointsParameters();
        listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
        listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
        listEndpointsParameters.setOnboardingResponse(onboardingResponse);
        listEndpointsParameters.setUnfilteredList(false);
        return sendAsync(listEndpointsParameters);
    }

    @Override
    public MqttAsyncMessageSendingResult sendMessageToListAllAsync(
            OnboardingResponse onboardingResponse) {
        var listEndpointsParameters = new ListEndpointsParameters();
        listEndpointsParameters.setDirection(Endpoints.ListEndpointsQuery.Direction.SEND_RECEIVE);
        listEndpointsParameters.setTechnicalMessageType(SystemMessageType.EMPTY);
        listEndpointsParameters.setOnboardingResponse(onboardingResponse);
        listEndpointsParameters.setUnfilteredList(true);
        return sendAsync(listEndpointsParameters);
    }

    @Override
    public agrirouter.response.payload.account.Endpoints.ListEndpointsResponse unsafeDecode(
            ByteString message) throws InvalidProtocolBufferException {
        return agrirouter.response.payload.account.Endpoints.ListEndpointsResponse.parseFrom(message);
    }
}
