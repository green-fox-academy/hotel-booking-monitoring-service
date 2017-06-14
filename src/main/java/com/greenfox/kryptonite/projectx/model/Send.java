package com.greenfox.kryptonite.projectx.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.NoArgsConstructor;


import java.net.URI;
import java.net.URISyntaxException;

@NoArgsConstructor
public class Send {

  private URI rabbitMqUrl;
  private final static String QUEUE_NAME = "kryptonite";

  public void send() throws Exception {

    try {
      rabbitMqUrl = new URI(System.getenv("RABBITMQ_BIGWIG_TX_URL"));
    } catch(URISyntaxException e) {
      e.getStackTrace();
    }
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
    factory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
    factory.setHost(rabbitMqUrl.getHost());
    factory.setPort(rabbitMqUrl.getPort());
    factory.setVirtualHost(rabbitMqUrl.getPath().substring(1));
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World!";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  public void consume() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(RABBITMQ_BIGWIG_URL);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
