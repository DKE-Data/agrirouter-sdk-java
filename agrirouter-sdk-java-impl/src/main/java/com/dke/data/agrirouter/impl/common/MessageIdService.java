package com.dke.data.agrirouter.impl.common;

import java.util.UUID;

/**
 * Service to provide a unique application message id.
 */
public class MessageIdService {

    /**
     * Generate a unique message id by using the internal UUID implementation.
     *
     * @return -
     */
    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }
}
