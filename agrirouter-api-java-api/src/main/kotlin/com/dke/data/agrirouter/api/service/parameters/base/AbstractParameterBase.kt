package com.dke.data.agrirouter.api.service.parameters.base

open class AbstractParameterBase : DynamicAttributesStorage() {

    var applicationMessageId : String? = null

    var teamsetContextId : String? = null

    var sequenceNumber : Int = 1

}