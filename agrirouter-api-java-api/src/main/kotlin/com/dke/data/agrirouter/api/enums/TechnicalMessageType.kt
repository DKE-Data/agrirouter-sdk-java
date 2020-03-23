package com.dke.data.agrirouter.api.enums

enum class TechnicalMessageType(val key: String, val isChunkable: Boolean, val hasToBeBase64Encoded: Boolean) {
    EMPTY("", false, false),
    DKE_CLOUD_ONBOARD_ENDPOINTS("dke:cloud_onboard_endpoints", false, false),
    DKE_CLOUD_OFFBOARD_ENDPOINTS("dke:cloud_offboard_endpoints", false, false),
    DKE_CAPABILITIES("dke:capabilities", false, false),
    DKE_SUBSCRIPTION("dke:subscription", false, false),
    DKE_LIST_ENDPOINTS("dke:list_endpoints", false, false),
    DKE_LIST_ENDPOINTS_UNFILTERED("dke:list_endpoints_unfiltered", false, false),
    DKE_FEED_CONFIRM("dke:feed_confirm", false, false),
    DKE_FEED_DELETE("dke:feed_delete", false, false),
    DKE_FEED_MESSAGE_QUERY("dke:feed_message_query", false, false),
    DKE_FEED_HEADER_QUERY("dke:feed_header_query", false, false),

    ISO_11783_DEVICE_DESCRIPTION("iso:11783:-10:device_description:protobuf", false, false),
    ISO_11783_TIME_LOG("iso:11783:-10:time_log:protobuf", false, false),

    ISO_11783_TASKDATA_ZIP("iso:11783:-10:taskdata:zip", true, true),
    SHP_SHAPE_ZIP("shp:shape:zip", true, true),
    DOC_PDF("doc:pdf", true, true),
    IMG_JPEG("img:jpeg", true, true),
    IMG_PNG("img:png", true, true),
    IMG_BMP("img:bmp", true, true),
    VID_AVI("vid:avi", true, true),
    VID_MP4("vid:mp4", true, true),
    VID_WMV("vid:wmv", true, true),
}
