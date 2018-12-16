package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import org.jetbrains.annotations.NotNull

class CloudEndpointOnboardingMessageParameters : ParameterValidation {

    @NotNull
    lateinit var endpointId: String

    @NotNull
    lateinit var endpointName: String

}