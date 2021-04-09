package com.dke.data.agrirouter.api.dto.encoding

class DecodeMessageResponse {

    lateinit var responseEnvelope: agrirouter.response.Response.ResponseEnvelope

    lateinit var responsePayloadWrapper: agrirouter.response.Response.ResponsePayloadWrapper

}