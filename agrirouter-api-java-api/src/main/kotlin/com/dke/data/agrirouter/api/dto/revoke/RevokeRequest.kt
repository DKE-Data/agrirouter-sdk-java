package com.dke.data.agrirouter.api.dto.revoke


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class RevokeRequest {

    lateinit var accountId: String

    lateinit var endpointIds: Array<String>;

    lateinit var UTCTimestamp: String

    lateinit var timeZone: String

}