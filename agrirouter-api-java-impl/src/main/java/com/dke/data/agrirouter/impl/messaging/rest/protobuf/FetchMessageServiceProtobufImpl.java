package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import static java.util.Optional.empty;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sap.iotservices.common.protobuf.gateway.CommandResponseListProtos;
import com.sap.iotservices.common.protobuf.gateway.CommandResponseMessageProtos;
import com.sap.iotservices.common.protobuf.gateway.CommandResponseProtos;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.eclipse.paho.client.mqttv3.internal.websocket.Base64;

public class FetchMessageServiceProtobufImpl extends MessageFetcher implements FetchMessageService {

  public Optional<List<FetchMessageResponse>> poll(
      FetchMessageParameters parameters, int maxTries, long interval) {
    parameters.validate();
    int nrOfTries = 0;
    while (nrOfTries < maxTries) {
      Response response =
          RequestFactory.securedNativeProtobufRequest(
                  parameters.getOnboardingResponse().getConnectionCriteria().getCommands(),
                  parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                  parameters.getOnboardingResponse().getAuthentication().getSecret(),
                  CertificationType.valueOf(
                      parameters.getOnboardingResponse().getAuthentication().getType()),
                  RequestFactory.DIRECTION_INBOX)
              .get();
      this.assertStatusCodeIsOk(response.getStatus());
      CommandResponseListProtos.CommandResponseList commandResponseList = null;
      try {
        commandResponseList =
            CommandResponseListProtos.CommandResponseList.parseFrom(
                response.readEntity(byte[].class));
        if (commandResponseList.getCommandsList().size() > 0) {
          List<FetchMessageResponse> fetchMessageResponseList =
              new ArrayList<FetchMessageResponse>();
          for (CommandResponseProtos.CommandResponse commandResponse :
              commandResponseList.getCommandsList()) {
            FetchMessageResponse fetchMessageResponse = new FetchMessageResponse();
            fetchMessageResponse.setSensorAlternateId(commandResponse.getSensorAlternateId());
            fetchMessageResponse.setCapabilityAlternateId(
                commandResponse.getCapabilityAlternateId());
            CommandResponseProtos.CommandResponse.Command protobufMessage =
                commandResponse.getCommand();
            Any messageBuffer = Any.parseFrom(protobufMessage.getValues(0).toByteArray());
            CommandResponseMessageProtos.CommandResponseMessage commandResponseMessage =
                CommandResponseMessageProtos.CommandResponseMessage.parseFrom(
                    messageBuffer.getValue().toByteArray());
            Message message = new Message();
            byte[] binaryMessage = commandResponseMessage.getMessage().toByteArray();
            message.setMessage(Base64.encodeBytes(binaryMessage));
            fetchMessageResponse.setCommand(message);
            fetchMessageResponseList.add(fetchMessageResponse);
          }
          return Optional.of(fetchMessageResponseList);
        }
      } catch (InvalidProtocolBufferException e) {
        e.printStackTrace();
        return Optional.empty();
      }
      nrOfTries++;
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        System.out.println("Interrupted Sleep");
        e.printStackTrace();
      }
    }
    return empty();
  }
}
