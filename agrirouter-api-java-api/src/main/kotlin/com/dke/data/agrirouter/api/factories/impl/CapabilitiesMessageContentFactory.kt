package com.dke.data.agrirouter.api.factories.impl

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.factories.impl.parameters.CapabilitiesMessageParameters
import com.google.protobuf.ByteString

class CapabilitiesMessageContentFactory {

    fun message(parameters: CapabilitiesMessageParameters): ByteString {
        parameters.validate()
        val messageContent = Capabilities.CapabilitySpecification.newBuilder()
        parameters.capabilities.forEach { c ->
            val capability = Capabilities.CapabilitySpecification.Capability.newBuilder()
            capability.technicalMessageType = c.technicalMessageType
            capability.direction = c.direction
            messageContent.addCapabilities(capability)
        }
        messageContent.appCertificationId = parameters.appCertificationId
        messageContent.appCertificationVersionId = parameters.appCertificationVersionId
        messageContent.enablePushNotifications = parameters.enablePushNotifications
        return messageContent.build().toByteString()
    }

}
