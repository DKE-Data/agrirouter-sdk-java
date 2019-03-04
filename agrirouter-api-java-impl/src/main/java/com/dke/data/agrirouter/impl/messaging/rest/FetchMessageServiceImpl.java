package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.impl.messaging.rest.json.FetchMessageServiceJSONImpl;

public class FetchMessageServiceImpl extends FetchMessageServiceJSONImpl {

  /**
   * FetchMessageServiceImpl
   *
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     FetchMessageServiceJSONImpl or FetchMessageServiceProtobufImpl instead
   */
  @Deprecated
  public FetchMessageServiceImpl() {
    super();
  }
}
