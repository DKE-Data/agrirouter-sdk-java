package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import com.google.protobuf.ByteString

/**
 * Parameters class. Encapsulation for the services.
 */
class PayloadParameters : AbstractParameterBase(), ParameterValidation {

    var typeUrl: String = ""

    var value: ByteString? = null

    override fun technicalValidation() {
        isBlank(typeUrl)
        nullCheck(value)
    }

}