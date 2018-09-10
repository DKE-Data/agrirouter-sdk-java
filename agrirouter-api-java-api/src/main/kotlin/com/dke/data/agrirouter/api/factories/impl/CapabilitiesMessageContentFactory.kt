package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.CapabilitiesMessageParameters
import com.google.protobuf.ByteString
import java.util.*

class CapabilitiesMessageContentFactory : MessageContentFactory<CapabilitiesMessageParameters> {

    override fun message(vararg parameters: CapabilitiesMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = Capabilities.CapabilitySpecification.newBuilder()
        val first = Arrays.stream(parameters).findFirst()

        if (first.isPresent) {
            first.get().capabilities.forEach { c ->
                val capability = Capabilities.CapabilitySpecification.Capability.newBuilder()
                capability.technicalMessageType = c.technicalMessageType
                capability.direction = c.direction
                messageContent.addCapabilities(capability)
            }
            val messageParameters = first.get()
            messageContent.appCertificationId = messageParameters.appCertificationId
            messageContent.appCertificationVersionId = messageParameters.appCertificationVersionId
        }
        return messageContent.build().toByteString()
    }
}
