package com.dke.data.agrirouter.api.service.parameters.base

abstract class AbstractParameterBase : DynamicAttributesStorage() {

    var applicationMessageId: String? = null

    var teamsetContextId: String? = null

    var sequenceNumber: Int = 0

}