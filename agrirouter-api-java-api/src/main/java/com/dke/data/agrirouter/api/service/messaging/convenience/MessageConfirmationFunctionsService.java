package com.dke.data.agrirouter.api.service.messaging.convenience;

import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationForAllPendingMessagesParameters;

public interface MessageConfirmationFunctionsService {

  void confirmAllPendingMessagesWithValidation(
      MessageConfirmationForAllPendingMessagesParameters parameters);

  void confirmAllPendingMessages(MessageConfirmationForAllPendingMessagesParameters parameters);
}
