package com.dke.data.agrirouter.impl;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes("application/x-protobuf")
public class ProtobufEntityRequestReader implements MessageBodyReader<Message> {
  public boolean isReadable(
      Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    System.out.println("IsReadable was called, sooo sad");
    return Message.class.isAssignableFrom(type);
  }

  public Message readFrom(
      Class<Message> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType,
      MultivaluedMap<String, String> httpHeaders,
      InputStream entityStream)
      throws WebApplicationException {
    try {
      Method newBuilder = type.getMethod("newBuilder");
      GeneratedMessage.Builder builder = (GeneratedMessage.Builder) newBuilder.invoke(type);
      return builder.mergeFrom(entityStream).build();
    } catch (Exception e) {
      throw new WebApplicationException(e);
    }
  }
}
