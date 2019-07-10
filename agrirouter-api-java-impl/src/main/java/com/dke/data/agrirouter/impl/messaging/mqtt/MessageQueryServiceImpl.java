package com.dke.data.agrirouter.impl.messaging.mqtt;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.mqtt.MessageQueryService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class MessageQueryServiceImpl extends MqttService
    implements com.dke.data.agrirouter.api.service.messaging.MessageQueryService,
        MessageSender,
        MessageDecoder<FeedResponse.MessageQueryResponse> {

  private final MessageQueryService messageQueryService;

  public MessageQueryServiceImpl(MqttClient mqttClient) {
    super(mqttClient);
    this.messageQueryService =
        new MessageQueryService(
            mqttClient,
            new EncodeMessageServiceImpl(),
            TechnicalMessageType.DKE_FEED_MESSAGE_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    return this.messageQueryService.send(parameters);
  }

  @Override
  public FeedResponse.MessageQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.MessageQueryResponse.parseFrom(message);
  }
}
