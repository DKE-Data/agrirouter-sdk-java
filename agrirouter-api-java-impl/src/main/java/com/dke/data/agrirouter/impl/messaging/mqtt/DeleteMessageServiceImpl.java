package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessage;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.factories.impl.DeleteMessageMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.DeleteMessageMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.DeleteMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.MessageSender;

import java.util.Collections;
import java.util.Objects;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DeleteMessageServiceImpl extends MqttService
        implements DeleteMessageService, MessageSender {

    private final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    public DeleteMessageServiceImpl(IMqttClient mqttClient) {
        super(mqttClient);
    }

    @Override
    public String send(DeleteMessageParameters parameters) {
        parameters.validate();
        try {
            EncodeMessage encodedMessageResponse = this.encodeMessage(parameters);
            SendMessageParameters sendMessageParameters = new SendMessageParameters();
            sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
            sendMessageParameters.setEncodedMessages(
                    Collections.singletonList(encodedMessageResponse.getEncodedMessage()));
            String messageAsJson = this.createMessageBody(sendMessageParameters);
            byte[] payload = messageAsJson.getBytes();
            this.getMqttClient()
                    .publish(
                            parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
                            new MqttMessage(payload));
            return encodedMessageResponse.getApplicationMessageID();
        } catch (MqttException e) {
            throw new CouldNotSendMqttMessageException(e);
        }
    }

    private EncodeMessage encodeMessage(DeleteMessageParameters parameters) {
        MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
        String applicationMessageID = MessageIdService.generateMessageId();
        messageHeaderParameters.setApplicationMessageId(applicationMessageID);
        messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_DELETE);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
        DeleteMessageMessageParameters deleteMessageMessageParameters = new DeleteMessageMessageParameters();
        deleteMessageMessageParameters.setMessageIds(Objects.requireNonNull(parameters.getMessageIds()));
        deleteMessageMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
        deleteMessageMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
        deleteMessageMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());
        PayloadParameters payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(FeedRequests.MessageDelete.getDescriptor().getFullName());
        payloadParameters.setValue((new DeleteMessageMessageContentFactory()).message(deleteMessageMessageParameters));
        String encodedMessage = this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
        return new EncodeMessage(applicationMessageID, encodedMessage);
    }
}
