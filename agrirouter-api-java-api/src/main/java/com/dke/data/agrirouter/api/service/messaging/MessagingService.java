package com.dke.data.agrirouter.api.service.messaging;

public interface MessagingService<T> {

  String send(T parameters);
}
