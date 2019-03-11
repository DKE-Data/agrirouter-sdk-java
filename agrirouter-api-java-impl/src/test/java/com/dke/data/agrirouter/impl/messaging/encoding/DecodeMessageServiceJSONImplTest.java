package com.dke.data.agrirouter.impl.messaging.encoding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.DecodeMessageServiceJSONImpl;
import org.apache.xerces.impl.dv.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DecodeMessageServiceJSONImplTest {

  private static final String ENCODED_MESSAGE =
      "IwoBMRABGhBka2U6Y2FwYWJpbGl0aWVzQgoIsdGL1AUQwIQ9TgpMCjt"
          + "hZ3Jpcm91dGVyLnJlcXVlc3QucGF5bG9hZC5lbmRwb2ludC5DYXBhYmlsaXR5U3BlY2lm"
          + "aWNhdGlvbhINc2VjcmV0TWVzc2FnZQ==";

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenNullDecodeShouldNotFail() {
    DecodeMessageService decodeMessageService = new DecodeMessageServiceJSONImpl();
    byte[] message = null;
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodeMessageService.decode(message));
  }

  @Test
  void givenWhitespaceMessageDecodeShouldNotFail() {
    DecodeMessageService decodeMessageService = new DecodeMessageServiceJSONImpl();
    byte[] message = new byte[]{};
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodeMessageService.decode(message));
  }

  @Test
  void givenValidEncodedPasswordDecodeShouldNotFail() {
    DecodeMessageService decodeMessageService = new DecodeMessageServiceJSONImpl();
    byte[] decodedMessageBytes = Base64.decode(DecodeMessageServiceJSONImplTest.ENCODED_MESSAGE);
    DecodeMessageResponse decodedMessage =
        decodeMessageService.decode(decodedMessageBytes);
    Assertions.assertEquals(
        decodedMessage.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8(),
        "secretMessage");
  }

  @Test
  void givenNullEnvironmentDecodeShouldNotFail() {
    DecodeMessageService decodeMessageService = new DecodeMessageServiceJSONImpl();
    byte[] decodedMessageBytes = Base64.decode(DecodeMessageServiceJSONImplTest.ENCODED_MESSAGE);
    DecodeMessageResponse decodedMessage =
        decodeMessageService.decode(decodedMessageBytes);
    Assertions.assertEquals(
        decodedMessage.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8(),
        "secretMessage");
  }
}
