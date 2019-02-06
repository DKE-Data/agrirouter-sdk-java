package com.dke.data.agrirouter.api.factories.impl

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration
import com.dke.data.agrirouter.api.factories.impl.parameters.CloudEndpointOffboardingMessageParameters
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class CloudEndpointOffboardingMessageContentFactory {

    fun message(parameters: CloudEndpointOffboardingMessageParameters): ByteString {
        parameters.validate()
        val messageContent = CloudVirtualizedAppRegistration.OffboardingRequest.newBuilder()
        messageContent.addAllEndpoints(parameters.endpointIds)
        return messageContent.build().toByteString()
    }

}