package com.dke.data.agrirouter.api.factories.impl

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration
import agrirouter.feed.request.FeedRequests
import com.dke.data.agrirouter.api.factories.MessageContentFactory
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters
import com.dke.data.agrirouter.api.factories.impl.parameters.OnboardCloudEndpointMessageParameters
import com.google.protobuf.ByteString
import java.util.*

/**
 * Implementation of a message content factory.
 */
class OnboardCloudEndpointMessageContentFactory : MessageContentFactory<OnboardCloudEndpointMessageParameters> {

    override fun message(vararg parameters: OnboardCloudEndpointMessageParameters): ByteString {
        parameters.forEach { p -> p.validate() }
        val messageContent = CloudVirtualizedAppRegistration.OnboardingRequest.newBuilder()
        parameters.forEach { p ->
            val builder = CloudVirtualizedAppRegistration.OnboardingRequest.EndpointRegistrationDetails.newBuilder()
            builder.id = p.endpointId
            builder.name = p.endpointName
        }
        return messageContent.build().toByteString()
    }

}