package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import com.rabbitmq.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Getter
@Setter
@NoArgsConstructor
public class MessageQueueService {

  private URI rabbitMqUrl;
  private final static String QUEUE_NAME = "kryptonite";
  private static final String EXCHANGE_NAME = "log";
  Message jsonMessage = new Message();
  String extractJsonMessage = "IT ISN'T WORKING";
  ConnectionFactory factory = new ConnectionFactory();

  public void setUpQueue(ConnectionFactory newFactory) {
    newFactory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
    newFactory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
    newFactory.setHost(rabbitMqUrl.getHost());
    newFactory.setPort(rabbitMqUrl.getPort());
    newFactory.setVirtualHost(rabbitMqUrl.getPath().substring(1));
  }

  public void send(String message) throws Exception {
    try {
      rabbitMqUrl = new URI(System.getenv("RABBITMQ_BIGWIG_TX_URL"));
    } catch (URISyntaxException e) {
      e.getStackTrace();
    }

    setUpQueue(factory);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

    channel.basicPublish(EXCHANGE_NAME, "", null,
        jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  public String consume() throws Exception {

    String message = "";

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    try {
      rabbitMqUrl = new URI(System.getenv("RABBITMQ_BIGWIG_RX_URL"));
    } catch (URISyntaxException e) {
      e.getStackTrace();
    }
    setUpQueue(factory);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(
            " [x] Received '" + message + "'");
        System.out.println("LETS SEE " + message);
        extractJsonMessage = jsonMessage.receiveJsonMessage(message).getMessage();
      }
    };

    channel.basicConsume(QUEUE_NAME, true, consumer);
    try {
      Thread.sleep(30000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    System.out.println("this is the message" + message);
    return message;
  }



  public String extractMessage() {
    return extractJsonMessage;
  }
}
