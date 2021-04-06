package com.dke.data.agrirouter.api.service.messaging.encoding;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageParameterTuple;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import java.util.List;

/** Encoding of messages. */
public interface EncodeMessageService {

  /**
   * Encode a given message using the internal protobuf encoding mechanism.
   *
   * @param messageHeaderParameters -
   * @param payloadParameters -
   * @return -
   */
  String encode(
      MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters);

  /**
   * Encode a number of messages.
   *
   * @param messageParameterTuples -
   * @return -
   */
  List<String> encode(List<MessageParameterTuple> messageParameterTuples);

  /**
   * Chunk a message if necessary. The chunk information and all IDs will be set by the SDK and are
   * not longer in control of the application.
   *
   * @param messageHeaderParameters -
   * @param payloadParameters -
   * @return -
   */
  List<MessageParameterTuple> chunk(
      MessageHeaderParameters messageHeaderParameters,
      PayloadParameters payloadParameters,
      OnboardingResponse onboardingResponse);
}
