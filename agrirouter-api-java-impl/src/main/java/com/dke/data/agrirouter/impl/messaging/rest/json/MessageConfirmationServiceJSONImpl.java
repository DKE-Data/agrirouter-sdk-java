package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.json.DecodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageConfirmationServiceImpl;

public class MessageConfirmationServiceJSONImpl extends MessageConfirmationServiceImpl {

  public MessageConfirmationServiceJSONImpl(Environment environment) {
    super(
        environment,
        new EncodeMessageServiceJSONImpl(),
        new DecodeMessageServiceJSONImpl(),
        new MessageSenderJSONImpl(),
        new MessageQueryServiceJSONImpl(environment),
        new FetchMessageServiceJSONImpl());
  }
}
