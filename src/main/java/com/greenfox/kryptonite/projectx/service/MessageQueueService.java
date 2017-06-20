package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
@NoArgsConstructor
public class MessageQueueService {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private URI rabbitMqUrl;
  private final static String QUEUE_NAME = "kryptonite2";
  private static final String EXCHANGE_NAME = "log";
  Message jsonMessage = new Message();
  private String temporaryMessage = "IT ISN'T WORKING";
  static final ExecutorService threadPool = Executors.newCachedThreadPool();

  public void send(String message) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    channel.basicPublish("", QUEUE_NAME, null, jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  public String consume() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    GetResponse getResponse = channel.basicGet(QUEUE_NAME, false);
    channel.close();
    connection.close();
    String temp = new String(getResponse.getBody());
    setTemporaryMessage(jsonMessage.receiveJsonMessage(temp).getMessage());
    return new String(getResponse.getBody());
  }

  public void setTemporaryMessage(String temporaryMessage) {
    this.temporaryMessage = temporaryMessage;
  }

  public String extractMessage(){
    return temporaryMessage;
  }
}
