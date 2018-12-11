package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import org.jetbrains.annotations.NotNull

class OffboardCloudEndpointMessageParameters : ParameterValidation {

    @NotNull
    lateinit var endpointIds
            : List<String>

}