package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationForAllPendingMessagesParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationParameters;

public interface MessageConfirmationService
    extends MessagingService<MessageConfirmationParameters> {

  void confirmAllPendingMessagesWithValidation(
      MessageConfirmationForAllPendingMessagesParameters parameters);

  void confirmAllPendingMessages(MessageConfirmationForAllPendingMessagesParameters parameters);
}
