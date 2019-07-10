package com.dke.data.agrirouter.api.service.messaging.convinience;

import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationForAllPendingMessagesParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationParameters;

public interface MessageConfirmationFunctionsService {

  void confirmAllPendingMessagesWithValidation(
          MessageConfirmationForAllPendingMessagesParameters parameters);

  void confirmAllPendingMessages(MessageConfirmationForAllPendingMessagesParameters parameters);
}
