package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotNull

class SubscriptionMessageParameters : ParameterValidation {
    class SubscriptionMessageEntry {
        @NotNull
        lateinit var technicalMessageType: TechnicalMessageType

        @NotNull
        lateinit var ddis: List<Int>

        var position: Boolean = false
    }

    @NotNull
    lateinit var list : List<SubscriptionMessageEntry>
}