package com.greenfox.kryptonite.projectx.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

  String message;
  String hostname;
  String date;

  public Message(String message, String hostname) {

    Date newDate = new Date();
    this.date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(newDate);
    this.message = message;
    this.hostname = hostname;
  }

  public String sendJsonMessage(String text) throws JsonProcessingException, URISyntaxException {

    Message sendMessage = new Message(text,
        new URI(System.getenv("RABBITMQ_BIGWIG_RX_URL")).getHost());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(sendMessage);
  }

  public Message receiveJsonMessage(String jsonMessage) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(jsonMessage, Message.class);
  }
}
