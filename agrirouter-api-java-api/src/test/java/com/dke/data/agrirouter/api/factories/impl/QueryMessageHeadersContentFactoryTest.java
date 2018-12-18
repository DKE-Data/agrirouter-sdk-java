package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.google.protobuf.ByteString;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class QueryMessageHeadersContentFactoryTest {

  @Test
  void givenValidParametersMessageShouldNotFail() {
    MessageQueryMessageParameters parameters = new MessageQueryMessageParameters();
    parameters.senderIds = Collections.singletonList("2");
    parameters.messageIds = Collections.singletonList("2");
    ByteString message = this.getInstanceToTest().message(parameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyParametersMessageShouldThrowException() {
    MessageQueryMessageParameters queryMessagesMessageParameters =
        new MessageQueryMessageParameters();
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(queryMessagesMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenParametersWithNullValuesMessageShouldNotFail() {
    MessageQueryMessageParameters queryMessagesMessageParameters =
        new MessageQueryMessageParameters();
    queryMessagesMessageParameters.senderIds = null;
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(queryMessagesMessageParameters));
  }

  private MessageQueryMessageContentFactory getInstanceToTest() {
    return new MessageQueryMessageContentFactory();
  }
}
