package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.ListEndpointsServiceImpl;

public class ListEndpointsServiceJSONImpl extends ListEndpointsServiceImpl {

  public ListEndpointsServiceJSONImpl(Environment environment) {
    super(environment, new SendMessageServiceJSONImpl(), new EncodeMessageServiceJSONImpl());
  }
}
