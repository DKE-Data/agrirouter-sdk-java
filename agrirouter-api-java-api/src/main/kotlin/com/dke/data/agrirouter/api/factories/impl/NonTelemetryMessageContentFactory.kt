package com.dke.data.agrirouter.api.factories.impl

import com.dke.data.agrirouter.api.factories.impl.parameters.NonTelemetryMessageParameters
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class NonTelemetryMessageContentFactory {

    fun message(parameters: NonTelemetryMessageParameters): ByteString {
        parameters.validate()
        return ByteString.copyFromUtf8(parameters.content)
    }

}