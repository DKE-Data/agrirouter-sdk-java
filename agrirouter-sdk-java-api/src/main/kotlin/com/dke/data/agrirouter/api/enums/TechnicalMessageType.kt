package com.dke.data.agrirouter.api.enums

enum class TechnicalMessageType(val key: String, val needsChunking: Boolean) {
    EMPTY("",false),
    DKE_CLOUD_ONBOARD_ENDPOINTS("dke:cloud_onboard_endpoints",false),
    DKE_CLOUD_OFFBOARD_ENDPOINTS("dke:cloud_offboard_endpoints",false),
    DKE_CAPABILITIES("dke:capabilities",false),
    DKE_SUBSCRIPTION("dke:subscription",false),
    DKE_LIST_ENDPOINTS("dke:list_endpoints",false),
    DKE_LIST_ENDPOINTS_UNFILTERED("dke:list_endpoints_unfiltered",false),
    DKE_FEED_CONFIRM("dke:feed_confirm",false),
    DKE_FEED_DELETE("dke:feed_delete",false),
    DKE_FEED_MESSAGE_QUERY("dke:feed_message_query",false),
    DKE_FEED_HEADER_QUERY("dke:feed_header_query",false),

    DKE_OTHER("dke:other",true),
    ISO_11783_TASKDATA_ZIP("iso:11783:-10:taskdata:zip",true),
    ISO_11783_DEVICE_DESCRIPTION("iso:11783:-10:device_description:protobuf",false),
    ISO_11783_TIME_LOG("iso:11783:-10:time_log:protobuf",false),
    SHP_SHAPE_ZIP("shp:shape:zip",true),
    DOC_PDF("doc:pdf",true),
    IMG_JPEG("img:jpeg",true),
    IMG_PNG("img:png",true),
    IMG_BMP("img:bmp",true),
    VID_AVI("vid:avi",true),
    VID_MP4("vid:mp4",true),
    VID_WMV("vid:wmv",true),
    GPS_INFO("gps:info",false),

    TESTING_PURPOSE_INVALID("SOME_INVALID_TYPE", true),
    TEST_OTHER("test:other", true);

}
