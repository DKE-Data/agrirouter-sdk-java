package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.endpoint.SubscriptionOuterClass
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class SubscriptionMessageContentFactory : MessageContentFactory<SubscriptionMessageParameters> {

    override fun message(vararg parameters: SubscriptionMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = SubscriptionOuterClass.Subscription.newBuilder()
        Arrays.stream(parameters).forEach { p ->
            val technicalMessageType = SubscriptionOuterClass.Subscription.MessageTypeSubscriptionItem.newBuilder()
            technicalMessageType.technicalMessageType = p.technicalMessageType.key

            technicalMessageType.addAllDdis(p.ddis)
            technicalMessageType.position = p.position
            messageContent.addTechnicalMessageTypes(technicalMessageType)
        }
        return messageContent.build().toByteString()
    }

}