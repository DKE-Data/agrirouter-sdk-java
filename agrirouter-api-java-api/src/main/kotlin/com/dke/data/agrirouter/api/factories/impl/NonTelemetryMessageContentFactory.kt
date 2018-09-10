package com.dke.data.agrirouter.api.factories.impl

import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.NonTelemetryMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class NonTelemetryMessageContentFactory : MessageContentFactory<NonTelemetryMessageParameters> {

    override fun message(vararg parameters: NonTelemetryMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val first = Arrays.stream(parameters).findFirst()
        if (first.isPresent) {
            val messageParameter = first.get()
            return ByteString.copyFromUtf8(messageParameter.content)
        }
        return ByteString.EMPTY
    }

}