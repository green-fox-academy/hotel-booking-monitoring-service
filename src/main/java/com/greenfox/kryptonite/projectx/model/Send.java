package com.greenfox.kryptonite.projectx.model;



import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Send {
  private final static String QUEUE_NAME = "kryptonite";

  public void send() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("https://greenfox-kryptonite.herokuapp.com");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World!";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

}
