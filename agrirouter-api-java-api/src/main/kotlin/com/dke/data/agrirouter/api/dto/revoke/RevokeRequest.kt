package com.dke.data.agrirouter.api.dto.revoke


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class RevokeRequest {

    lateinit var accountId: String

    lateinit var endpointIds: Array<String>;

    @SerializedName("UTCTimestamp")
    @JsonProperty("UTCTimestamp")
    lateinit var utcTimestamp: String

    lateinit var timeZone: String

}