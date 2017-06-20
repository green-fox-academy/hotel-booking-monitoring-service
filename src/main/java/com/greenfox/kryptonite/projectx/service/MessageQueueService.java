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
  private final static String QUEUE_NAME = "kryptonite2";
  private static final String EXCHANGE_NAME = "log";
  private Message jsonMessage = new Message();
  private String temporaryMessage = "Shit";

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

  public void consume() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(RABBIT_MQ_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    GetResponse getResponse = channel.basicGet(QUEUE_NAME, false);
    setTemporaryMessage(new String(getResponse.getBody()));

    channel.close();
    connection.close();

//    channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel){
//      @Override
//      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//              throws IOException {
//        try {
//          System.out.println("Received From Exchange : " + envelope.getExchange() + " With routing key " + envelope.getRoutingKey() + " Message: " + new String(body));
//          channel.basicAck(envelope.getDeliveryTag(), true);
//        } catch (Exception e) {
//          e.printStackTrace();
//          channel.basicReject(envelope.getDeliveryTag(), true);
//        }
//      }
//    });
//
    System.out.println("Ready to receive messages!");
  }

  public void setTemporaryMessage(String temporaryMessage) {
    this.temporaryMessage = temporaryMessage;
  }

  public String getTemporaryMessage() {
    return temporaryMessage;
  }
}