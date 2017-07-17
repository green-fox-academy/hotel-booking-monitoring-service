package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageQueueServiceTest {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";
  private MessageQueueService messageQueueService;

  @Before
  public void setup() throws Exception {
    this.messageQueueService = new MessageQueueService();
  }

  @Test
  public void testSend() throws Exception {
    int initialSize = messageQueueService.getCount("testqueue");
    messageQueueService.send(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", "TestMessage" );
    int currentSize = messageQueueService.getCount("testqueue");
    assertEquals(initialSize + 1, currentSize);
  }

  @Test
  public void testConsume() throws Exception {
    int initialSize = messageQueueService.getCount("testqueue");
    if (initialSize != 0) {
      messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", true, true);
      int currentSize = messageQueueService.getCount("testqueue");
      assertEquals(initialSize - 1, currentSize);
    }
  }

  @Test
  public void testRabbitMqConsumeParadox() throws Exception {
    messageQueueService.send(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", "TestMessage" );
    messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", true, true);
    Message message = new Message();
    assertEquals( "TestMessage", message.receiveJsonMessage(messageQueueService.getTemporaryMessage()).getMessage());
  }

  @Test
  public void testRabbitMqConsumeParadoxAssertNotEqual() throws Exception {
    messageQueueService.send(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", "TestMessage" );
    messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", true, true);
    Message message = new Message();
    assertNotEquals( "TestFalse", message.receiveJsonMessage(messageQueueService.getTemporaryMessage()).getMessage());
  }

  @Test
  public void testQueuedMessageCount() throws Exception {
    assertTrue(messageQueueService.getCount("testqueue") == 0);
  }

  @Test
  public void testQueuedMessageCountAssertFalse() throws Exception {
    assertFalse(messageQueueService.getCount("testqueue") == 1);
  }

  @Test
  public void testGetCount() throws Exception {
    assertEquals(0, (int) messageQueueService.getCount("testqueue"));
  }

  @Test
  public void testGetCountNotEquals() throws Exception {
    assertEquals(1, (int) messageQueueService.getCount("testqueue"));
  }
}