package com.dke.data.agrirouter.api.factories.impl

import agrirouter.feed.request.FeedRequests
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class MessageConfirmationMessageContentFactory : MessageContentFactory<MessageConfirmationMessageParameters> {

    override fun message(vararg parameters: MessageConfirmationMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = FeedRequests.MessageConfirm.newBuilder()
        val first = Arrays.stream(parameters).findFirst()
        if (first.isPresent) {
            val messageParameters = first.get()
            messageContent.addAllMessageIds(messageParameters.messageIds)
        }
        return messageContent.build().toByteString()
    }

}