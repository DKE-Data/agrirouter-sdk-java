package com.dke.data.agrirouter.api.factories.impl;

import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryMessagesMessageContentFactoryTest extends AbstractMessageContentFactoryTest<MessageQueryMessageContentFactory> {

    @Test
    void givenValidQueryMessagesMessageParameters_Message_ShouldNotFail() {
        List<String> messageIds = new ArrayList<>();
        messageIds.add("1");
        List<String> senderIds = new ArrayList<>();
        senderIds.add("2");
        MessageQueryMessageParameters queryMessagesMessageParameters = new MessageQueryMessageParameters();
        queryMessagesMessageParameters.messageIds = messageIds;
        queryMessagesMessageParameters.senderIds = senderIds;
        ByteString message = this.getInstanceToTest().message(queryMessagesMessageParameters);
        assertFalse(message.isEmpty());
    }

    @Test
    void givenEmptyMessageQueryMessageParameters_Message_ShouldThrowException() {
        MessageQueryMessageParameters queryMessagesMessageParameters = new MessageQueryMessageParameters();
        assertThrows(UninitializedPropertyAccessException.class,() -> this.getInstanceToTest().message(queryMessagesMessageParameters));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenMessageQueryMessageParametersWithNullValues_Message_ShouldNotFail() {
        MessageQueryMessageParameters queryMessagesMessageParameters = new MessageQueryMessageParameters();
        queryMessagesMessageParameters.messageIds = null;
        queryMessagesMessageParameters.senderIds = null;
        assertThrows(UninitializedPropertyAccessException.class,() -> this.getInstanceToTest().message(queryMessagesMessageParameters));
    }

    @Override
    protected MessageQueryMessageContentFactory getInstanceToTest() {
        return new MessageQueryMessageContentFactory();
    }
}
