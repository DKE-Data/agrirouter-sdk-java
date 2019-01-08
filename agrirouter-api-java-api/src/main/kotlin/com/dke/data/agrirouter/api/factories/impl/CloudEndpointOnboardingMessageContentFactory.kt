package com.dke.data.agrirouter.api.factories.impl

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration
import com.dke.data.agrirouter.api.factories.impl.parameters.CloudEndpointOnboardingMessageParameters
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class CloudEndpointOnboardingMessageContentFactory {

    fun message(vararg parameters: CloudEndpointOnboardingMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = CloudVirtualizedAppRegistration.OnboardingRequest.newBuilder()
        parameters.forEach { p ->
            val builder = CloudVirtualizedAppRegistration.OnboardingRequest.EndpointRegistrationDetails.newBuilder()
            builder.id = p.endpointId
            builder.name = p.endpointName
            messageContent.addOnboardingRequests(builder.build())
        }
        return messageContent.build().toByteString()
    }

}