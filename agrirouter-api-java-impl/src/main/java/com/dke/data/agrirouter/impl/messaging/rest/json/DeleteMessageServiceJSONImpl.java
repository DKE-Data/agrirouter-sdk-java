package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.DeleteMessageServiceImpl;

public class DeleteMessageServiceJSONImpl extends DeleteMessageServiceImpl {

  public DeleteMessageServiceJSONImpl() {
    super(new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }
}
