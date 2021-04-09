package com.dke.data.agrirouter.api.dto.xsuaa

import com.dke.data.agrirouter.api.dto.xsuaa.inner.ExtAttr
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class XsuaaResponse {

    @JsonProperty("access_token")
    lateinit var accessToken: String

    @JsonProperty("token_type")
    lateinit var tokenType: String

    @JsonProperty("expires_in")
    lateinit var expiresIn: String

    lateinit var scope: String

    @JsonProperty("ext_attr")
    lateinit var extAttr: ExtAttr


}
