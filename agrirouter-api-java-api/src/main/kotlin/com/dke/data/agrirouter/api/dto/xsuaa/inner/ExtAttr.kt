package com.dke.data.agrirouter.api.dto.xsuaa.inner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ExtAttr {
    lateinit var enhancer: String
}
