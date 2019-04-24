package com.dke.data.agrirouter.api.service.parameters.container

/**
 * Possibility to store additional parameters for custom reasons if needed.
 */
open class DynamicAttributesContainer {

    private var attributes: HashMap<String, Any> = HashMap()

    fun storeAttribute(key: String, value: Any) {
        attributes.put(key, value)
    }

    fun fetchAttribute(key: String): Any? {
        return attributes[key]
    }

    fun storeApplicationMessageId(applicationMessageId:String){
        this.attributes.put(APPLICATION_MESSAGE_ID, applicationMessageId)
    }

    fun fetchApplicationMessageId() : String? {
        return this.attributes.get(APPLICATION_MESSAGE_ID) as String?
    }

    companion object {
        const val APPLICATION_MESSAGE_ID = "APPLICATION_MESSAGE_ID"
    }

}
