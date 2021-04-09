package com.dke.data.agrirouter.api.service.parameters.base

/**
 * Possibility to store additional parameters for custom reasons if needed.
 */
open class DynamicAttributesStorage {

    private var attributes: HashMap<String, Any> = HashMap()

    fun storeAttribute(key: String, value: Any) {
        attributes.put(key, value)
    }

    fun fetchAttribute(key: String): Any? {
        return attributes[key]
    }

}
