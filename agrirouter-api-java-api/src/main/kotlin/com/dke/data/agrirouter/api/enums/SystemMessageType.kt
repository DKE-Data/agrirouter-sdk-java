package com.dke.data.agrirouter.api.enums

/**
 * Enum containing all the content message types the AR is supporting.
 */
enum class SystemMessageType(val key: String) {
    EMPTY(""),
    DKE_CLOUD_ONBOARD_ENDPOINTS("dke:cloud_onboard_endpoints"),
    DKE_CLOUD_OFFBOARD_ENDPOINTS("dke:cloud_offboard_endpoints"),
    DKE_CAPABILITIES("dke:capabilities"),
    DKE_SUBSCRIPTION("dke:subscription"),
    DKE_LIST_ENDPOINTS("dke:list_endpoints"),
    DKE_LIST_ENDPOINTS_UNFILTERED("dke:list_endpoints_unfiltered"),
    DKE_FEED_CONFIRM("dke:feed_confirm"),
    DKE_FEED_DELETE("dke:feed_delete"),
    DKE_FEED_MESSAGE_QUERY("dke:feed_message_query"),
    DKE_FEED_HEADER_QUERY("dke:feed_header_query"),

}
