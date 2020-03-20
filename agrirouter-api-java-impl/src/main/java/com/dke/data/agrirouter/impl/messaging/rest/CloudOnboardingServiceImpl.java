package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.service.messaging.CloudOnboardingService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

public class CloudOnboardingServiceImpl
    implements CloudOnboardingService, MessageSender, ResponseValidator, MessageEncoder {

  private final EncodeMessageService encodeMessageService;

  public CloudOnboardingServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  /**
   * Onboarding a virtual CU for an existing cloud application (incl. several checks).
   *
   * @param parameters Parameters for the onboarding.
   * @return -
   */
  @Override
  public String send(CloudOnboardingParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessageResponse = this.encode(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessageResponse.getEncodedMessage()));
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsValid(response.getNativeResponse().getStatus());
    return encodedMessageResponse.getApplicationMessageID();
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
