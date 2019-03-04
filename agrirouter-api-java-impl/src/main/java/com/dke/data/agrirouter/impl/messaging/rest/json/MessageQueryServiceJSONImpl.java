package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageQueryServiceImpl;

public class MessageQueryServiceJSONImpl extends MessageQueryServiceImpl<SendMessageRequest> {

  public MessageQueryServiceJSONImpl(Environment environment) {
    super(environment, new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }
}
