package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.SetCapabilityService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

public class SetCapabilityServiceImpl extends EnvironmentalService
    implements SetCapabilityService, MessageSender, MessageEncoder, ResponseValidator {

  private final EncodeMessageService encodeMessageService;

  public SetCapabilityServiceImpl(Environment environment) {
    super(environment);
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(SetCapabilitiesParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessage = this.encode(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodedMessage.getApplicationMessageID();
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return encodeMessageService;
  }
}
