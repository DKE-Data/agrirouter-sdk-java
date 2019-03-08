package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
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
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.ArrayList;
import java.util.List;

public class SetCapabilityServiceImpl extends EnvironmentalService
    implements SetCapabilityService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final MessageSender messageSender;

  /**
   * @param environment
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     SetCapabilityServiceJSONImpl or SetCapabilityServiceProtobufImpl instead
   */
  @Deprecated
  public SetCapabilityServiceImpl(Environment environment) {
    this(environment, new EncodeMessageServiceJSONImpl(), new MessageSenderJSONImpl());
  }

  public SetCapabilityServiceImpl(
      Environment environment,
      EncodeMessageService encodeMessageService,
      MessageSender messageSender) {
    super(environment);
    this.encodeMessageService = encodeMessageService;
    this.messageSender = messageSender;
  }

  @Override
  public String send(SetCapabilitiesParameters parameters) {
    parameters.validate();

    EncodeMessageResponse encodeMessageResponse = encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodeMessageResponse(encodeMessageResponse);

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodeMessageResponse.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(SetCapabilitiesParameters parameters) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID = MessageIdService.generateMessageId();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);

    messageHeaderParameters.setApplicationMessageSeqNo(1);
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_CAPABILITIES);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    List<Capabilities.CapabilitySpecification.Capability> capabilities = new ArrayList<>();

    parameters.getCapabilitiesParameters();
    parameters
        .getCapabilitiesParameters()
        .forEach(
            p -> {
              Capabilities.CapabilitySpecification.Capability.Builder capabilityBuilder =
                  Capabilities.CapabilitySpecification.Capability.newBuilder();
              capabilityBuilder.setTechnicalMessageType(p.technicalMessageType.getKey());
              capabilityBuilder.setDirection(p.direction);
              Capabilities.CapabilitySpecification.Capability capability =
                  capabilityBuilder.build();
              capabilities.add(capability);
            });

    CapabilitiesMessageParameters capabilitiesMessageParameters =
        new CapabilitiesMessageParameters();
    capabilitiesMessageParameters.setCapabilities(capabilities);
    capabilitiesMessageParameters.setAppCertificationId(parameters.getApplicationId());
    capabilitiesMessageParameters.setAppCertificationVersionId(
        parameters.getCertificationVersionId());

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        Capabilities.CapabilitySpecification.getDescriptor().getFullName());
    payloadParameters.setValue(
        new CapabilitiesMessageContentFactory().message(capabilitiesMessageParameters));

    return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return this.messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
