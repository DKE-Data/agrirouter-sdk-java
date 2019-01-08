package com.dke.data.agrirouter.api.service.messaging;

public interface MessagingService<T> {

  void send(T parameters);

  MessageSenderResponse sendWithoutValidation(T parameters);
}
