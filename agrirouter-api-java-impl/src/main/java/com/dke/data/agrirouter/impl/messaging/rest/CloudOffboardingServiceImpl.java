package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.service.messaging.CloudOffboardingService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

/** Service implementation. */
public class CloudOffboardingServiceImpl
    implements CloudOffboardingService, MessageSender, ResponseValidator, MessageEncoder {

  private final EncodeMessageService encodeMessageService;

  public CloudOffboardingServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  /**
   * Offboarding a virtual CU. Will deliver no result if the action was successful, if there's any
   * error an exception will be thrown.
   *
   * @param parameters Parameters for offboarding.
   */
  @Override
  public String send(CloudOffboardingParameters parameters) {
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
