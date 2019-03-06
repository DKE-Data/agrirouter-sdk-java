package com.dke.data.agrirouter.api.enums

enum class TechnicalMessageType(val key: String) {
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

    DKE_OTHER("dke:other"),
    ISO_11783_TASKDATA_ZIP("iso:11783:-10:taskdata:zip"),
    ISO_11783_DEVICE_DESCRIPTION("iso:11783:-10:device_description:protobuf"),
    ISO_11783_TIME_LOG("iso:11783:-10:time_log:protobuf"),
    SHP_SHAPE_ZIP("shp:shape:zip"),
    IMG_JPEG("img:jpeg"),
    IMG_PNG("img:png"),
    IMG_BMP("img:bmp"),
    VID_AVI("vid:avi"),
    VID_MP4("vid:mp4"),
    VID_WMV("vid:wmv"),

    TESTING_PURPOSE_INVALID("SOME_INVALID_TYPE"),
    TEST_OTHER ("test:other")
}
