package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class MessageQueueService {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private Message jsonMessage = new Message();
  private String temporaryMessage = "This shouldn't be appeared!";

  @Autowired
  PageViewService pageViewService;

  public void send(String hostUrl, String exchangeName, String queueName, String message) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(hostUrl);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
    if (queueName.equals("testEventQueue")) {
      pageViewService = new PageViewService();
      channel.basicPublish(exchangeName, queueName, null, pageViewService.sendJsonHotelEventQueue().getBytes("UTF-8"));
    } else {
      channel.basicPublish(exchangeName, queueName, null, jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));
    }
    channel.close();
    connection.close();
  }

  public void consume(String hostUrl, String exchangeName, String queueName, boolean bindQueue, boolean autoAck) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(hostUrl);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    if (bindQueue) {
      channel.queueBind(queueName, exchangeName, "");
    }
    GetResponse getResponse = channel.basicGet(queueName, autoAck);
    setTemporaryMessage(new String(getResponse.getBody()));

    channel.close();
    connection.close();
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