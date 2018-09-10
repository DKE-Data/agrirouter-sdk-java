package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.factories.impl.CapabilitiesMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.CapabilitiesMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.SetCapabilityService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.api.service.parameters.inner.Message;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class SetCapabilityServiceImpl extends EnvironmentalService implements SetCapabilityService, MessageSender, ResponseValidator {

    private final EncodeMessageService encodeMessageService;

    public SetCapabilityServiceImpl(Environment environment) {
        super(environment);
        this.encodeMessageService = new EncodeMessageServiceImpl();
    }

    @Override
    public void send(SetCapabilitiesParameters parameters) {
        parameters.validate();

        String messageId = MessageIdService.generateMessageId();

        String encodedMessage = encodeMessage(parameters);
        List<Message> messages = new ArrayList<Message>() {
            {
                Message message = new Message();
                message.setMessageId(messageId);
                message.setEncodedMessage(encodedMessage);
                add(message);
            }
        };

        SendMessageParameters sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        sendMessageParameters.setMessages(messages);

        MessageSenderResponse response = this.sendMessage(sendMessageParameters);

        this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK);

    }

    private String encodeMessage(SetCapabilitiesParameters parameters) {
        MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
        messageHeaderParameters.setApplicationMessageSeqNo(1);
        messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_CAPABILITIES);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        List<Capabilities.CapabilitySpecification.Capability> capabilities = new ArrayList<>();

        parameters.getCapabilitiesParameters();
        parameters.getCapabilitiesParameters().forEach(p -> {
            Capabilities.CapabilitySpecification.Capability.Builder capabilityBuilder = Capabilities.CapabilitySpecification.Capability.newBuilder();
            capabilityBuilder.setTechnicalMessageType(p.technicalMessageType.getKey());
            capabilityBuilder.setDirection(p.direction);
            Capabilities.CapabilitySpecification.Capability capability = capabilityBuilder.build();
            capabilities.add(capability);
        });

        CapabilitiesMessageParameters capabilitiesMessageParameters = new CapabilitiesMessageParameters();
        capabilitiesMessageParameters.setCapabilities(capabilities);
        capabilitiesMessageParameters.setAppCertificationId(parameters.getApplicationId());
        capabilitiesMessageParameters.setAppCertificationVersionId(parameters.getCertificationVersionId());

        PayloadParameters payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(Capabilities.CapabilitySpecification.getDescriptor().getFullName());
        payloadParameters.setValue(new CapabilitiesMessageContentFactory().message(capabilitiesMessageParameters));

        return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    }

}
