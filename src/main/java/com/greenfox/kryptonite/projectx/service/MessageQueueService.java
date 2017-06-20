package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
@NoArgsConstructor
public class MessageQueueService {

  private URI rabbitMqUrl;
  private final static String QUEUE_NAME = "kryptonite";
  private static final String EXCHANGE_NAME = "log";
  Message jsonMessage = new Message();
  private String temporaryMessage = "Shit";
  static final ExecutorService threadPool = Executors.newCachedThreadPool();

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
    } catch(URISyntaxException e) {
      e.getStackTrace();
    }
    ConnectionFactory factory = new ConnectionFactory();
    setUpQueue(factory);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, jsonMessage.sendJsonMessage(message).getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  public String consume() throws Exception {
    try {
      rabbitMqUrl = new URI(System.getenv("RABBITMQ_BIGWIG_RX_URL"));
    } catch(URISyntaxException e) {
      e.getStackTrace();
    }
    ConnectionFactory factory = new ConnectionFactory();
    setUpQueue(factory);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        final String message = new String(body, "UTF-8");
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            setTemporaryMessage(message);
          }
        };
        threadPool.submit(runnable);
        System.out.println(" [x] Received '" + jsonMessage.receiveJsonMessage(message).getMessage() + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
    return temporaryMessage;
  }

  public void setTemporaryMessage(String temporaryMessage) {
    this.temporaryMessage = temporaryMessage;
  }
}
