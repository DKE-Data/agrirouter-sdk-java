package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.endpoint.SubscriptionOuterClass
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class SubscriptionMessageContentFactory {

    fun message(parameters: SubscriptionMessageParameters): ByteString {
        parameters.validate()
        val messageContent = SubscriptionOuterClass.Subscription.newBuilder()

        parameters.list.forEach{ entry ->
            val technicalMessageType = SubscriptionOuterClass.Subscription.MessageTypeSubscriptionItem.newBuilder()
            technicalMessageType.setTechnicalMessageType(entry.technicalMessageType.key)
            technicalMessageType.addAllDdis(entry.ddis)
            technicalMessageType.position = entry.position
            messageContent.addTechnicalMessageTypes(technicalMessageType)
        }
        return messageContent.build().toByteString()
    }

}