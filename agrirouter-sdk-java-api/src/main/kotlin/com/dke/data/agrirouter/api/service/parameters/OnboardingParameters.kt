package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.ApplicationType
import com.dke.data.agrirouter.api.enums.CertificationType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class OnboardingParameters : AbstractParameterBase(), ParameterValidation {

    var applicationId: String? = null

    var uuid: String? = null

    var certificationVersionId: String? = null

    var gatewayId: String? = null

    var certificationType: CertificationType? = null

    var applicationType: ApplicationType? = null

    var registrationCode: String? = null

    override fun technicalValidation() {
        isBlank("applicationId",applicationId)
        isBlank("uuid",uuid)
        isBlank("certificationVersionId",certificationVersionId)
        isBlank("gatewayId",gatewayId)
        nullCheck("certificationType",certificationType)
        nullCheck("applicationType",applicationType)
        isBlank("registrationCode",registrationCode)
    }
}