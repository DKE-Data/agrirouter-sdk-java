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

    fun setApplicationMessageId(applicationMessageId:String){
        this.attributes.put(APPLICATION_MESSAGE_ID, applicationMessageId)
    }

    fun getApplicationMessageId() : String? {
        return this.attributes.get(APPLICATION_MESSAGE_ID) as String?
    }

    fun setTeamsetContextId(teamsetContextId:String){
        this.attributes.put(TEAMSET_CONTEXT_ID, teamsetContextId)
    }

    fun getTeamsetContextId() : String? {
        return this.attributes.get(TEAMSET_CONTEXT_ID) as String?
    }

    fun setSequenceNumber(sequenceNumber: Int){
        this.attributes.put(SEQUENCE_NUMBER, sequenceNumber)
    }

    fun getSequenceNumber() : Int {
        var sequenceNumber: Int = this.attributes.get(SEQUENCE_NUMBER) as Int
        if(sequenceNumber == 0){
            sequenceNumber = 1
        }
        return sequenceNumber
    }

    companion object {
        const val APPLICATION_MESSAGE_ID = "APPLICATION_MESSAGE_ID"
        const val TEAMSET_CONTEXT_ID = "TEAMSET_CONTEXT_ID"
        const val SEQUENCE_NUMBER = "SEQUENCE_NUMBER"
    }
}
