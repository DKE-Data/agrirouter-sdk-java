package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotNull

class MessageConfirmationMessageParameters : ParameterValidation {

    @NotNull
    lateinit var messageIds: List<String>

}