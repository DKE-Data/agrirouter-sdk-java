package com.dke.data.agrirouter.api.factories.impl

import agrirouter.feed.request.FeedRequests
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class MessageConfirmationMessageContentFactory {

    fun message(parameters: MessageConfirmationMessageParameters): ByteString {
        parameters.validate()
        val messageContent = FeedRequests.MessageConfirm.newBuilder()
        messageContent.addAllMessageIds(parameters.messageIds)
        return messageContent.build().toByteString()
    }

}