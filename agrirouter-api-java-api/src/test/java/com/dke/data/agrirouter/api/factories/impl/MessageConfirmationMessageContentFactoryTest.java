package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters;
import com.google.protobuf.ByteString;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class MessageConfirmationMessageContentFactoryTest {

  @Test
  void givenValidQueryMessagesMessageParametersMessageShouldNotFail() {
    MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
    parameters.messageIds = Collections.singletonList("2");
    ByteString message = this.getInstanceToTest().message(parameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyQueryMessagesMessageParametersMessageShouldThrowException() {
    MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(parameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenQueryMessagesMessageParametersWithNullValuesMessageShouldNotFail() {
    MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
    parameters.messageIds = null;
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(parameters));
  }

  protected MessageConfirmationMessageContentFactory getInstanceToTest() {
    return new MessageConfirmationMessageContentFactory();
  }
}
