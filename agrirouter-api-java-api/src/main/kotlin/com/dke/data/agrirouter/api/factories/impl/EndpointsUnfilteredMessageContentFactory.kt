package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.account.Endpoints
import com.dke.data.agrirouter.api.service.parameters.EndpointsUnfilteredMessageParameters
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class EndpointsUnfilteredMessageContentFactory {

    fun message(parameters: EndpointsUnfilteredMessageParameters): ByteString {
        parameters.validate()
        val messageContent = Endpoints.ListEndpointsQuery.newBuilder()
        messageContent.direction = parameters.direction
        messageContent.technicalMessageType = parameters.technicalMessageType.key
        return messageContent.build().toByteString()
    }

}