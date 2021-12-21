package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.CertificationType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class SecuredOnboardingParameters : AbstractParameterBase(), ParameterValidation {

    var applicationId: String? = null

    var privateKey: String? = null

    var publicKey: String? = null

    var uuid: String? = null

    var certificationVersionId: String? = null

    var gatewayId: String? = null

    var certificationType: CertificationType? = null

    var registrationCode: String? = null

    override fun technicalValidation() {
        isBlank("applicationId",applicationId)
        isBlank("privateKey",privateKey)
        isBlank("publicKey",publicKey)
        isBlank("uuid",uuid)
        isBlank("certificationVersionId",certificationVersionId)
        isBlank("gatewayId",gatewayId)
        isBlank("registrationCode",registrationCode)
        nullCheck("certificationType",certificationType)
    }

}