package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.account.Endpoints
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.service.parameters.EndpointsUnfilteredMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class EndpointsUnfiltererdMessageContentFactory : MessageContentFactory<EndpointsUnfilteredMessageParameters> {

    override fun message(vararg parameters: EndpointsUnfilteredMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = Endpoints.ListEndpointsQuery.newBuilder()
        val first = Arrays.stream(parameters).findFirst()
        if (first.isPresent) {
            val messageParameters = first.get()
            messageContent.direction = messageParameters.direction
            messageContent.technicalMessageType = messageParameters.technicalMessageType.key
        }
        return messageContent.build().toByteString()
    }

}