package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

class QueryMessagesMessageContentFactoryTest {

  @Test
  void givenValidQueryMessagesMessageParametersMessageShouldNotFail() {
    List<String> messageIds = new ArrayList<>();
    messageIds.add("1");
    List<String> senderIds = new ArrayList<>();
    senderIds.add("2");
    MessageQueryMessageParameters queryMessagesMessageParameters =
        new MessageQueryMessageParameters();
    queryMessagesMessageParameters.messageIds = messageIds;
    queryMessagesMessageParameters.senderIds = senderIds;
    ByteString message = this.getInstanceToTest().message(queryMessagesMessageParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyMessageQueryMessageParametersMessageShouldThrowException() {
    MessageQueryMessageParameters queryMessagesMessageParameters =
        new MessageQueryMessageParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(queryMessagesMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenMessageQueryMessageParametersWithNullValuesMessageShouldNotFail() {
    MessageQueryMessageParameters queryMessagesMessageParameters =
        new MessageQueryMessageParameters();
    queryMessagesMessageParameters.messageIds = null;
    queryMessagesMessageParameters.senderIds = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(queryMessagesMessageParameters));
  }

  private MessageQueryMessageContentFactory getInstanceToTest() {
    return new MessageQueryMessageContentFactory();
  }
}
