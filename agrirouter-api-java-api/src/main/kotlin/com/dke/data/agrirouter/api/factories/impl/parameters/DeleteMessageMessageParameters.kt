package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotNull

class DeleteMessageMessageParameters : ParameterValidation {

    @NotNull
    lateinit var messageIds: List<String>

    @NotNull
    lateinit var senderIds: List<String>

    var sentFromInSeconds: Long? = null

    var sentToInSeconds: Long? = null

}