package com.dke.data.agrirouter.api.enums

import agrirouter.technicalmessagetype.Gps

/**
 * Enum containing all the content message types the AR is supporting.
 */
enum class ContentMessageType(private val key: String, private val typeUrl: String) : TechnicalMessageType {
    ISO_11783_TASKDATA_ZIP("iso:11783:-10:taskdata:zip", ""),

    //FIXME Since the spec is not public, we can only use those literals.
    ISO_11783_DEVICE_DESCRIPTION("iso:11783:-10:device_description:protobuf", "types.agrirouter.com\\efdi.ISO11783_TaskData"),
    ISO_11783_TIME_LOG("iso:11783:-10:time_log:protobuf", "types.agrirouter.com\efdi.TimeLog"),

    SHP_SHAPE_ZIP("shp:shape:zip", ""),
    DOC_PDF("doc:pdf", ""),
    IMG_JPEG("img:jpeg", ""),
    IMG_PNG("img:png", ""),
    IMG_BMP("img:bmp", ""),
    VID_AVI("vid:avi", ""),
    VID_MP4("vid:mp4", ""),
    VID_WMV("vid:wmv", ""),
    GPS_INFO("gps:info", Gps.GPSList.getDescriptor().fullName);

    override fun getKey(): String {
        return key
    }

    override fun getTypeUrl(): String {
        return typeUrl
    }
}
