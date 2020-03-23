package com.dke.data.agrirouter.impl.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Message for sequence number generation.
 */
public class SequenceNumberService {

    private static final AtomicLong globalSeqNo = new AtomicLong();

    /**
     * Get the next sequence number for sending messages.
     *
     * @return -
     */
    public static long next() {
        return globalSeqNo.addAndGet(1);
    }

}
