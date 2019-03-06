package com.dke.data.agrirouter.impl.onboard.cloud.json;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.impl.messaging.encoding.json.DecodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.FetchMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.onboard.cloud.OnboardingServiceImpl;

public class OnboardingServiceJSONImpl extends OnboardingServiceImpl<SendMessageRequest> {
  public OnboardingServiceJSONImpl(){
    super(new EncodeMessageServiceJSONImpl(),
      new MessageSenderJSONImpl(),
      new FetchMessageServiceJSONImpl(),
      new DecodeMessageServiceJSONImpl());
  }
}
