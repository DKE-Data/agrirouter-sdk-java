package com.dke.data.agrirouter.api.service.messaging;

interface MessagingService<T> {

  String send(T parameters);
}
