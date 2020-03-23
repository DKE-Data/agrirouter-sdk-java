package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.service.parameters.SendContentMessageParameters;

/** Sending a content message to the AR. */
public interface SendContentMessageService extends MessagingService<SendContentMessageParameters> {
  /// <summary>
  /// Maximum value the AR can handle.
  /// </summary>
  int MAXIMUM_SUPPORTED = 1000000 - (int) (1000000 * 0.05);

  /// <summary>
  /// Half of a megabyte.
  /// </summary>
  int HALF_OF_THE_MAXIMUM = MAXIMUM_SUPPORTED / 2;

  /// <summary>
  /// Quarter of a megabyte.
  /// </summary>
  int QUARTER_OF_THE_MAXIMUM = MAXIMUM_SUPPORTED / 4;
}
