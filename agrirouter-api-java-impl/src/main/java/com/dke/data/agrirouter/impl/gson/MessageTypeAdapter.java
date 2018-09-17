package com.dke.data.agrirouter.impl.gson;

import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class MessageTypeAdapter extends TypeAdapter<Message> {

  @Override
  public void write(JsonWriter jsonWriter, Message message) throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(message.getMessage());
    jsonWriter.value(Long.parseLong(message.getTimestamp()));
    jsonWriter.endArray();
  }

  @Override
  public Message read(JsonReader jsonReader) throws IOException {
    throw new RuntimeException("Not yet supported.");
  }
}
