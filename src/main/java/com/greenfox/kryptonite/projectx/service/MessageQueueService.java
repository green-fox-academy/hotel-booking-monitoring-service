package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class MessageQueueService {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String QUEUE_NAME = "kryptonite2";
  private final String EXCHANGE_NAME = "log";
  private Message jsonMessage = new Message();
  private String temporaryMessage = "Shit";

  public void send(String message) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    channel.basicPublish(EXCHANGE_NAME,"", null, jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));

    channel.close();
    connection.close();
  }

  public void sendToEvents(String message) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.basicPublish("", "events", null, jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));

    channel.close();
    connection.close();
  }

  public void consume() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
    GetResponse getResponse = channel.basicGet(QUEUE_NAME, true);
    setTemporaryMessage(new String(getResponse.getBody()));

    channel.close();
    connection.close();

    System.out.println("Consume method run without problem!");
  }

  public Integer getCount(String queueName) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    AMQP.Queue.DeclareOk declare = channel.queueDeclarePassive(queueName);
    return declare.getMessageCount();
  }
}