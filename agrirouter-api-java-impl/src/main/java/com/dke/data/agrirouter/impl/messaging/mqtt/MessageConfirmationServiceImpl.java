package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.service.messaging.MessageConfirmationService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Collections;

public class MessageConfirmationServiceImpl extends MqttService
        implements MessageConfirmationService, MessageSender, MessageEncoder {

    private final EncodeMessageService encodeMessageService;

    public MessageConfirmationServiceImpl(MqttClient mqttClient) {
        super(mqttClient);
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public String send(MessageConfirmationParameters parameters) {
        parameters.validate();
        EncodedMessage encodedMessage = this.encode(parameters);
        SendMessageParameters sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setEncodedMessages(
                Collections.singletonList(encodedMessage.getEncodedMessage()));
        String messageAsJson = this.createMessageBody(sendMessageParameters);
        byte[] payload = messageAsJson.getBytes();
        try {
            this.getMqttClient()
                    .publish(
                            parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
                            new MqttMessage(payload));
            return encodedMessage.getApplicationMessageID();
        } catch (MqttException e) {
            throw new CouldNotSendMqttMessageException(e);
        }
    }

    @Override
    public EncodeMessageService getEncodeMessageService() {
        return this.encodeMessageService;
    }
}
