package com.dke.data.agrirouter.api.service.messaging;

interface MessagingService<T> {

  void send(T parameters);
}
