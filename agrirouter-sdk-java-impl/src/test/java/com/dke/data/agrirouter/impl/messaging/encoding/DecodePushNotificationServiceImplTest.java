package com.dke.data.agrirouter.impl.messaging.encoding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.feed.push.notification.PushNotificationOuterClass;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodePushNotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DecodePushNotificationServiceImplTest {

  private static final String ENCODED_MESSAGE =
      "OAgAEAwiJGY5MzVmZGRkLThkOTItNGZjMi1hMzZmLTBjNzJjZWY3NTE4NyoMCKuxwe"
          + "UFEIDrrr0DlgMKkwMKR3R5cGVzLmFncmlyb3V0ZXIuY29tL2Fncmlyb3V0ZXIuZmVlZC5wdXNoLm5vdGlmaWNhdGlvbi5QdXNoTm90aW"
          + "ZpY2F0aW9uEscCCsQCCp4BCiRiMzhlNWRiYy1iZGJmLTQ5ZGEtOGRjOS1jOGRiMDJhM2VjZjYSCWRrZTpvdGhlchoAKJ0BMgoIrrHB5Q"
          + "UQwIQ9OAFCJGRmOTdhYzgwLTA0ZWYtNDUzYS05OTE4LTk1OTMzNjUyOTI2MEoMCKuxweUFEID0qL4DUiQyNDhhOWJjZi0yYjIyLTQ0Nz"
          + "YtYTIwMi0wMDgyMTgxNWU5M2YSoAESnQFbcnE1Ml9naXZlblNpbmdsZVB1c2hOb3RpZmljYXRpb25SZWNlaXZlcl9QdXNoTm90aWZpY2"
          + "F0aW9uX1Nob3VsZEJlRGVsaXZlcmVkVG9UaGVPdXRib3hdIE1FU1NBR0UgQ09OVEVOVCBGT1IgVEVTVElORywgR0VORVJBVEVEIEFUIF"
          + "syMDE5LTA0LTEyVDA5OjIxOjUwLjY0OVpd";

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenNullDecodeShouldNotFail() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    String message = null;
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodePushNotificationService.decode(message));
  }

  @Test
  void givenEmptyMessageDecodeShouldNotFail() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodePushNotificationService.decode(""));
  }

  @Test
  void givenWhitespaceMessageDecodeShouldNotFail() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodePushNotificationService.decode("   "));
  }

  @Test
  void givenValidEncodedPasswordDecodeShouldNotFail() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    PushNotificationOuterClass.PushNotification pushNotification =
        decodePushNotificationService.decode(DecodePushNotificationServiceImplTest.ENCODED_MESSAGE);
    assertEquals(
        "[rq52_givenSinglePushNotificationReceiver_PushNotification_ShouldBeDeliveredToTheOutbox] MESSAGE CONTENT FOR TESTING, GENERATED AT [2019-04-12T09:21:50.649Z]",
        pushNotification.getMessages(0).getContent().getValue().toStringUtf8());
  }

  @Test
  void givenWrongEncodedPasswordDecodeShouldThrowException() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    String wrongEncodedMessage = "Wrong Message";
    assertThrows(
        IllegalArgumentException.class,
        () -> decodePushNotificationService.decode(wrongEncodedMessage));
  }

  @Test
  void givenNullEnvironmentDecodeShouldNotFail() {
    DecodePushNotificationService decodePushNotificationService =
        new DecodePushNotificationServiceImpl();
    PushNotificationOuterClass.PushNotification pushNotification =
        decodePushNotificationService.decode(DecodePushNotificationServiceImplTest.ENCODED_MESSAGE);
    assertEquals(
        "[rq52_givenSinglePushNotificationReceiver_PushNotification_ShouldBeDeliveredToTheOutbox] MESSAGE CONTENT FOR TESTING, GENERATED AT [2019-04-12T09:21:50.649Z]",
        pushNotification.getMessages(0).getContent().getValue().toStringUtf8());
  }
}
