package com.dke.data.agrirouter.api.dto.encoding.inner

class Header {

    lateinit var applicationMessageId: String

    var applicationMessageSeqNo: Int = 0

    lateinit var technicalMessageType: String

    lateinit var mode: String

}
