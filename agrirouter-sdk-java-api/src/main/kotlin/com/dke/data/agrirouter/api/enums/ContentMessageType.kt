package com.dke.data.agrirouter.api.enums

import agrirouter.technicalmessagetype.Gps

/**
 * Enum containing all the content message types the AR is supporting.
 */
@Suppress("unused")
enum class ContentMessageType(
    private val key: String,
    private val typeUrl: String,
    private val needsBase64Encoding: Boolean
) : TechnicalMessageType {
    ISO_11783_TASKDATA_ZIP("iso:11783:-10:taskdata:zip", "", true),
    SHP_SHAPE_ZIP("shp:shape:zip", "", true),
    DOC_PDF("doc:pdf", "", true),
    IMG_JPEG("img:jpeg", "", true),
    IMG_PNG("img:png", "", true),
    IMG_BMP("img:bmp", "", true),
    VID_AVI("vid:avi", "", true),
    VID_MP4("vid:mp4", "", true),
    VID_WMV("vid:wmv", "", true),
    GPS_INFO("gps:info", Gps.GPSList.getDescriptor().fullName, false),

    //FIXME Since the spec is not public, we can only use those literals.
    ISO_11783_DEVICE_DESCRIPTION(
        "iso:11783:-10:device_description:protobuf",
        "types.agrirouter.com/efdi.ISO11783_TaskData",
        false
    ),
    ISO_11783_TIME_LOG("iso:11783:-10:time_log:protobuf", "types.agrirouter.com/efdi.TimeLog", false);

    override fun getKey(): String {
        return key
    }

    override fun getTypeUrl(): String {
        return typeUrl
    }

    override fun needsBase64EncodingAndHasToBeChunkedIfNecessary(): Boolean {
        return needsBase64Encoding
    }

}
