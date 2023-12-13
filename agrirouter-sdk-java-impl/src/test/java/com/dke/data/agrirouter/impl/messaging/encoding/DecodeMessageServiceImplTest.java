package com.dke.data.agrirouter.impl.messaging.encoding;

import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DecodeMessageServiceImplTest {

    private static final String ENCODED_MESSAGE =
            "IwoBMRABGhBka2U6Y2FwYWJpbGl0aWVzQgoIsdGL1AUQwIQ9TgpMCjt"
                    + "hZ3Jpcm91dGVyLnJlcXVlc3QucGF5bG9hZC5lbmRwb2ludC5DYXBhYmlsaXR5U3BlY2lm"
                    + "aWNhdGlvbhINc2VjcmV0TWVzc2FnZQ==";

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenNullDecodeShouldNotFail() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        String message = null;
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> decodeMessageService.decode(message));
    }

    @Test
    void givenEmptyMessageDecodeShouldNotFail() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        Assertions.assertThrows(IllegalArgumentException.class, () -> decodeMessageService.decode(""));
    }

    @Test
    void givenWhitespaceMessageDecodeShouldNotFail() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> decodeMessageService.decode("   "));
    }

    @Test
    void givenValidEncodedPasswordDecodeShouldNotFail() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodedMessage =
                decodeMessageService.decode(DecodeMessageServiceImplTest.ENCODED_MESSAGE);
        Assertions.assertEquals(
                decodedMessage.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8(),
                "secretMessage");
    }

    @Test
    void givenWrongEncodedPasswordDecodeShouldThrowException() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var wrongEncodedMessage = "Wrong Message";
        assertThrows(
                IllegalArgumentException.class, () -> decodeMessageService.decode(wrongEncodedMessage));
    }

    @Test
    void givenNullEnvironmentDecodeShouldNotFail() {
        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodedMessage =
                decodeMessageService.decode(DecodeMessageServiceImplTest.ENCODED_MESSAGE);
        Assertions.assertEquals(
                decodedMessage.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8(),
                "secretMessage");
    }
}
