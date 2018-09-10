package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.google.protobuf.ByteString
import javax.validation.constraints.NotNull

class PayloadParameters : ParameterValidation {

    @NotNull
    var typeUrl: String = ""

    @NotNull
    lateinit var value: ByteString

}