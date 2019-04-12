package com.dke.data.agrirouter.api.service.messaging.encoding;

import agrirouter.feed.push.notification.PushNotificationOuterClass;

public interface DecodePushNotificationService {

  /**
   * Decoding a given push notification using the intenral protobuf implementation.
   *
   * @param message -
   * @return -
   */
  PushNotificationOuterClass.PushNotification decode(String message);
}
