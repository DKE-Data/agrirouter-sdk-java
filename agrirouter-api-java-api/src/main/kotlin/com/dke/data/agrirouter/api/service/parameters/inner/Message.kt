package com.dke.data.agrirouter.api.service.parameters.inner

import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotNull

@ToString
class Message : ParameterValidation {

    @NotNull
    lateinit var messageId: String

    @NotNull
    lateinit var encodedMessage: String

}