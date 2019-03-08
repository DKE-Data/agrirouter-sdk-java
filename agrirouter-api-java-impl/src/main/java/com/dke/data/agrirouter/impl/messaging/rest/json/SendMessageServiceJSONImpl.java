package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SendMessageServiceImpl;

public class SendMessageServiceJSONImpl extends SendMessageServiceImpl {
  public SendMessageServiceJSONImpl() {
    super(new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }
}
