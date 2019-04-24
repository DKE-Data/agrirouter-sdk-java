package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.feed.push.notification.PushNotificationOuterClass;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodePushNotificationException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodePushNotificationService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.StringUtils;

public class DecodePushNotificationServiceImpl extends NonEnvironmentalService
    implements DecodePushNotificationService {

  private final DecodeMessageServiceImpl decodeMessageService;

  public DecodePushNotificationServiceImpl() {
    this.decodeMessageService = new DecodeMessageServiceImpl();
  }

  @Override
  public PushNotificationOuterClass.PushNotification decode(String encodedResponse) {
    this.logMethodBegin(encodedResponse);

    if (StringUtils.isBlank(encodedResponse)) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }
    try {
      DecodeMessageResponse decodedMessagesResponse =
          this.decodeMessageService.decode(encodedResponse);
      if (decodedMessagesResponse.getResponseEnvelope().getType()
          == Response.ResponseEnvelope.ResponseBodyType.PUSH_NOTIFICATION) {
        return decodedMessagesResponse
            .getResponsePayloadWrapper()
            .getDetails()
            .unpack(PushNotificationOuterClass.PushNotification.class);
      } else {
        throw new CouldNotDecodePushNotificationException(
            "This was not a push notification, please use common message decoding to decode the message itself.");
      }
    } catch (InvalidProtocolBufferException e) {
      throw new CouldNotDecodePushNotificationException(e);
    }
  }
}
