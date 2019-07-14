package com.dke.data.agrirouter.api.service.parameters

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class SetSubscriptionParameters : AbstractParameterBase(), ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    class Subscription : ParameterValidation {
        @NotNull
        lateinit var technicalMessageType: TechnicalMessageType

        var ddis: List<Int> = ArrayList();

        var position: Boolean = false
    }

    var subscriptionParameters: List<Subscription> = ArrayList()

}