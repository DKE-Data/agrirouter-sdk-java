package com.dke.data.agrirouter.api.dto.onboard

class RouterDevice{

    class Authentication{

        lateinit var type: String

        lateinit var secret: String

        lateinit var certificate: String

    }

    class ConnectionCriteria{

        lateinit var clientId:String

        lateinit var gatewayId:String

        lateinit var host:String

        var port:Int = 0
    }

    var authentication: Authentication = Authentication();

    var connectionCriteria: ConnectionCriteria = ConnectionCriteria();

    lateinit var deviceAlternateId:String

}