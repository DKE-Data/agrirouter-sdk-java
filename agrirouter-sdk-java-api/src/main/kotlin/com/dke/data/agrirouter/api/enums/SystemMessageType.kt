package com.dke.data.agrirouter.api.enums

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration
import agrirouter.feed.request.FeedRequests
import agrirouter.request.payload.account.Endpoints
import agrirouter.request.payload.endpoint.Capabilities
import agrirouter.request.payload.endpoint.SubscriptionOuterClass

/**
 * Enum containing all the content message types the AR is supporting.
 */
enum class SystemMessageType(private val key: String, private val typeUrl: String) : TechnicalMessageType {
    EMPTY("", ""),
    DKE_CLOUD_ONBOARD_ENDPOINTS("dke:cloud_onboard_endpoints", CloudVirtualizedAppRegistration.OnboardingRequest.getDescriptor().fullName),
    DKE_CLOUD_OFFBOARD_ENDPOINTS("dke:cloud_offboard_endpoints", CloudVirtualizedAppRegistration.OffboardingRequest.getDescriptor().fullName),
    DKE_CAPABILITIES("dke:capabilities", Capabilities.CapabilitySpecification.getDescriptor().fullName),
    DKE_SUBSCRIPTION("dke:subscription", SubscriptionOuterClass.Subscription.getDescriptor().fullName),
    DKE_LIST_ENDPOINTS("dke:list_endpoints", Endpoints.ListEndpointsQuery.getDescriptor().fullName),
    DKE_LIST_ENDPOINTS_UNFILTERED("dke:list_endpoints_unfiltered", Endpoints.ListEndpointsQuery.getDescriptor().fullName),
    DKE_FEED_CONFIRM("dke:feed_confirm", FeedRequests.MessageConfirm.getDescriptor().fullName),
    DKE_FEED_DELETE("dke:feed_delete", FeedRequests.MessageDelete.getDescriptor().fullName),
    DKE_FEED_MESSAGE_QUERY("dke:feed_message_query", FeedRequests.MessageQuery.getDescriptor().fullName),
    DKE_FEED_HEADER_QUERY("dke:feed_header_query", FeedRequests.MessageQuery.getDescriptor().fullName);

    override fun getKey(): String {
        return key
    }

    override fun getTypeUrl(): String {
        return typeUrl
    }

}
