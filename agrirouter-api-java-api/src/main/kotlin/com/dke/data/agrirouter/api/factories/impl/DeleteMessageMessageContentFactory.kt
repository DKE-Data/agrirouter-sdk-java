package com.dke.data.agrirouter.api.factories.impl

import agrirouter.feed.request.FeedRequests
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.DeleteMessageMessageParameters
import com.dke.data.agrirouter.api.util.TimestampUtil
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class DeleteMessageMessageContentFactory : MessageContentFactory<DeleteMessageMessageParameters> {

    override fun message(vararg parameters: DeleteMessageMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = FeedRequests.MessageDelete.newBuilder()
        val first = Arrays.stream(parameters).findFirst()
        if (first.isPresent) {
            val messageParameters = first.get()
            messageContent.addAllMessageIds(messageParameters.messageIds)
            messageContent.addAllSenders(messageParameters.senderIds)
            if(null != messageParameters.sentFromInSeconds || null!=messageParameters.sentToInSeconds) {
                val validityPeriod = FeedRequests.ValidityPeriod.newBuilder()
                if (null != messageParameters.sentFromInSeconds) {
                    validityPeriod.sentFrom = TimestampUtil().seconds(messageParameters.sentFromInSeconds!!)
                }
                if (null != messageParameters.sentToInSeconds) {
                    validityPeriod.sentTo = TimestampUtil().seconds(messageParameters.sentToInSeconds!!)
                }
                messageContent.setValidityPeriod(validityPeriod)
            }
        }
        return messageContent.build().toByteString()
    }

}