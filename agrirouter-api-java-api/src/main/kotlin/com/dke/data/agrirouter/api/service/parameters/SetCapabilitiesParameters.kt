package com.dke.data.agrirouter.api.service.parameters

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class SetCapabilitiesParameters : AbstractParameterBase(), ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    @NotNull
    @NotBlank
    lateinit var applicationId: String

    @NotNull
    @NotBlank
    lateinit var certificationVersionId: String

    lateinit var capabilitiesParameters: List<CapabilityParameters>

    class CapabilityParameters : ParameterValidation {

        @NotNull
        lateinit var technicalMessageType: TechnicalMessageType

        @NotNull
        lateinit var direction: Capabilities.CapabilitySpecification.Direction

    }

    override fun validate() {
        super.validate()
        capabilitiesParameters.stream().forEach { c -> c.validate() }
    }

}