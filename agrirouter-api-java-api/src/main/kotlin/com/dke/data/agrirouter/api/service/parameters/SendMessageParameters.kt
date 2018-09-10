package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.inner.Message
import lombok.ToString
import javax.validation.constraints.NotNull

@ToString
open class SendMessageParameters : ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    @NotNull
    lateinit var messages: List<Message>

    override fun validate() {
        super.validate()
        messages.forEach { m -> m.validate() }
    }

}