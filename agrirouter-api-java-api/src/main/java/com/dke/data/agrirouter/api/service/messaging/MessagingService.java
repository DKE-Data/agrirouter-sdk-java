package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.exception.*;

interface MessagingService<T> {

  void send(T parameters)
      throws InvalidUrlForRequestException, UnauthorizedRequestException, ForbiddenRequestException,
          CouldNotCreateDynamicKeyStoreException, UnexpectedHttpStatusException,
          CouldNotEncodeMessageException;
}
