package com.dke.data.agrirouter.api.service.parameters

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class SetCapabilitiesParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    var applicationId: String? = null

    var certificationVersionId: String? = null

    var capabilitiesParameters: List<CapabilityParameters>? = null

    var enablePushNotifications: Capabilities.CapabilitySpecification.PushNotification = Capabilities.CapabilitySpecification.PushNotification.DISABLED

    class CapabilityParameters : ParameterValidation {

        var technicalMessageType: TechnicalMessageType? = null

        var direction: Capabilities.CapabilitySpecification.Direction? = null

        override fun technicalValidation() {
            nullCheck("technicalMessageType",technicalMessageType)
            nullCheck("direction",direction)
        }

    }

    override fun technicalValidation() {
        nullCheck("onboardingResponse",onboardingResponse)
        isBlank("applicationId",applicationId)
        isBlank("certificationVersionId",certificationVersionId)
        nullCheck("capabilitiesParameters",capabilitiesParameters)
        capabilitiesParameters?.forEach { c -> c.validate() }
    }

}