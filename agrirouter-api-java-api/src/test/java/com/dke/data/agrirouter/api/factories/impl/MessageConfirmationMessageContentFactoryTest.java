package com.dke.data.agrirouter.api.factories.impl;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageConfirmationMessageContentFactoryTest extends AbstractMessageContentFactoryTest<MessageConfirmationMessageContentFactory> {

    @Test
    void givenValidQueryMessagesMessageParameters_Message_ShouldNotFail() {
        MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
        parameters.messageIds = Collections.singletonList("2");
        ByteString message = this.getInstanceToTest().message(parameters);
        assertFalse(message.isEmpty());
    }

    @Test
    void givenEmptyQueryMessagesMessageParameters_Message_ShouldThrowException() {
        MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
        assertThrows(IllegalParameterDefinitionException.class,() -> this.getInstanceToTest().message(parameters));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenQueryMessagesMessageParametersWithNullValues_Message_ShouldNotFail() {
        MessageConfirmationMessageParameters parameters = new MessageConfirmationMessageParameters();
        parameters.messageIds = null;
        assertThrows(IllegalParameterDefinitionException.class,() -> this.getInstanceToTest().message(parameters));
    }

    @Override
    protected MessageConfirmationMessageContentFactory getInstanceToTest() {
        return new MessageConfirmationMessageContentFactory();
    }
}
