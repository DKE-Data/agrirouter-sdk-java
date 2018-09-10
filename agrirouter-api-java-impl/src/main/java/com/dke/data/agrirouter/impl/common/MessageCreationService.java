package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.service.parameters.inner.Message;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service to create a collection of messages to send.
 */
public class MessageCreationService {

    /**
     * Create a collection of messages which can be send using the API.
     *
     * @param messageId      -
     * @param encodedMessage -
     * @return -
     */
    public static List<Message> create(String messageId, String encodedMessage) {
        return create(Collections.singletonMap(messageId, encodedMessage));
    }

    /**
     * Create a collection of messages which can be send using the API.
     *
     * @param messageIdsAndEncodedMessages -
     * @return -
     */
    private static List<Message> create(Map<String, String> messageIdsAndEncodedMessages) {
        return messageIdsAndEncodedMessages.entrySet().stream().map(entry -> {
            Message message = new Message();
            message.setMessageId(entry.getKey());
            message.setEncodedMessage(entry.getValue());
            return message;
        }).collect(Collectors.toList());
    }

}
