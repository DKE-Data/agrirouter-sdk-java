package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation

/**
 * Parameters class. Encapsulation for the services.
 */
class RevokeParameters : ParameterValidation {

    var applicationId: String? = null

    var accountId: String? = null

    var endpointIds: List<String>? = null

    var privateKey: String? = null

    var publicKey: String? = null

    override fun technicalValidation() {
        isBlank("applicationId",applicationId)
        isBlank("accountId",accountId)
        isBlank("privateKey",privateKey)
        isBlank("publicKey",publicKey)
        nullCheck("endpointIds",endpointIds)
    }

    override fun businessValidation() {
        nullOrEmpty("endpointIds",endpointIds)
    }
}