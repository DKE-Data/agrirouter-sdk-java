package com.dke.data.agrirouter.api.messaging

import javax.ws.rs.core.Response

/**
 * Wrapper for the response from the server.
 */
class MessageSendingResponse(val nativeResponse: Response)