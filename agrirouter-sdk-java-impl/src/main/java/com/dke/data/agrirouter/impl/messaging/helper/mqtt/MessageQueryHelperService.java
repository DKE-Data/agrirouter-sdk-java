package com.dke.data.agrirouter.impl.messaging.helper.mqtt;

import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.mqtt.HiveMqttClientWrapper;
import com.dke.data.agrirouter.api.mqtt.PahoMqttClientWrapper;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.helper.QueryAllMessagesParameterCreator;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.Collections;
import java.util.Objects;

public class MessageQueryHelperService extends MqttService
        implements MessageSender, MessageEncoder, QueryAllMessagesParameterCreator {

    private final EncodeMessageService encodeMessageService;
    private final TechnicalMessageType technicalMessageType;

    public MessageQueryHelperService(
            IMqttClient mqttClient,
            EncodeMessageService encodeMessageService,
            TechnicalMessageType technicalMessageType) {
        super(new PahoMqttClientWrapper(mqttClient));
        this.encodeMessageService = encodeMessageService;
        this.technicalMessageType = technicalMessageType;
    }

    public MessageQueryHelperService(
            Mqtt3AsyncClient mqttClient,
            EncodeMessageService encodeMessageService,
            TechnicalMessageType technicalMessageType) {
        super(new HiveMqttClientWrapper(mqttClient));
        this.encodeMessageService = encodeMessageService;
        this.technicalMessageType = technicalMessageType;
    }

    public String send(MessageQueryParameters parameters) {
        this.logMethodBegin(parameters);

        this.getNativeLogger().trace("Validate parameters.");
        parameters.validate();
        this.getNativeLogger().trace("Encode message.");
        var encodedMessage = this.encode(this.technicalMessageType, parameters);

        this.getNativeLogger().trace("Build message parameters.");
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));

        this.getNativeLogger().trace("Send and fetch message response.");
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
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
