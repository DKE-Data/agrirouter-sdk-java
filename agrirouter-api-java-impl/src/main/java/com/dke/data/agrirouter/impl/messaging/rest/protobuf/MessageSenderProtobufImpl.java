package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import static com.dke.data.agrirouter.impl.RequestFactory.MEDIA_TYPE_PROTOBUF;

import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequestProtobuf;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.exception.WrongFormatForMessageException;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.google.protobuf.*;
import com.sap.iotservices.common.protobuf.gateway.MeasureProtos;
import com.sap.iotservices.common.protobuf.gateway.MeasureRequestMessageProtos;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MessageSenderProtobufImpl implements MessageSender {

  public MessageRequestProtobuf createSendMessageRequest(
      SendMessageParameters sendMessageParameters) {
    sendMessageParameters.validate();
    EncodeMessageResponse.EncodeMessageResponseProtobuf encodeMessageResponseProtobuf = null;
    if (sendMessageParameters.getEncodeMessageResponse()
        instanceof EncodeMessageResponse.EncodeMessageResponseProtobuf) {
      encodeMessageResponseProtobuf =
          (EncodeMessageResponse.EncodeMessageResponseProtobuf)
              sendMessageParameters.getEncodeMessageResponse();
    } else {
      throw new WrongFormatForMessageException("Trying to pass JSON Message to Protobuf MessageSender");

    }

    MeasureProtos.MeasureRequest.Builder measureMessageBuilder =
        MeasureProtos.MeasureRequest.newBuilder()
            .setCapabilityAlternateId(
                sendMessageParameters.onboardingResponse.capabilityAlternateId)
            .setSensorAlternateId(sendMessageParameters.onboardingResponse.sensorAlternateId)
            .setTimestamp(UtcTimeService.now().toEpochSecond())
            .setSensorTypeAlternateId("");

    MeasureRequestMessageProtos.MeasureRequestMessage measureMessage =
        encodeMessageResponseProtobuf.getEncodedMessageProtobuf();

    MeasureProtos.MeasureRequest.Measure.Builder measureBuilder =
        MeasureProtos.MeasureRequest.Measure.newBuilder();

    ByteString protobufMessage = ByteString.copyFrom(measureMessage.getMessage().toByteArray());

    com.google.protobuf.Message message = BytesValue.newBuilder().setValue(protobufMessage).build();
    measureBuilder.addValues(Any.pack(message, "message"));

    Timestamp timestamp =
        Timestamp.newBuilder().setSeconds(UtcTimeService.now().toEpochSecond()).setNanos(0).build();

    String protobufTimeStampString =
        String.valueOf(timestamp.getSeconds() * 1000 + timestamp.getNanos());

    com.google.protobuf.Message protobufTimestamp =
        StringValue.newBuilder().setValue(protobufTimeStampString).build();
    measureBuilder.addValues(Any.pack(protobufTimestamp, "timestamp"));

    measureMessageBuilder.addMeasures(measureBuilder.build());

    MeasureProtos.MeasureRequest sendMessageProtobufRequest = measureMessageBuilder.build();

    return new MessageRequestProtobuf(sendMessageProtobufRequest);
  }

  public MessageSender.MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    MessageRequest messageRequest = this.createSendMessageRequest(parameters);
    MeasureProtos.MeasureRequest data =
        ((MessageRequestProtobuf) messageRequest).getSendMeasureRequest();

    Entity<MeasureProtos.MeasureRequest> protobufContent = Entity.entity(data, MEDIA_TYPE_PROTOBUF);
    Response response =
        RequestFactory.securedNativeProtobufRequest(
                parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
                parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                parameters.getOnboardingResponse().getAuthentication().getSecret(),
                CertificationType.valueOf(
                    parameters.getOnboardingResponse().getAuthentication().getType()),
                RequestFactory.DIRECTION_INBOX)
            .post(protobufContent);

    return new MessageSenderResponse(response);
  }
}
