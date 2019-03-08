package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageHeaderQueryServiceImpl;

public class MessageHeaderQueryServiceJSONImpl extends MessageHeaderQueryServiceImpl {

  public MessageHeaderQueryServiceJSONImpl(Environment environment) {
    super(environment, new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }
}
