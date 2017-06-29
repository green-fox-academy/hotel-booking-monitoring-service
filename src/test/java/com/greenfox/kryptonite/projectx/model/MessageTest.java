package com.greenfox.kryptonite.projectx.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class MessageTest {

  private Message message = new Message();

  @Test
  public void testSendJsonMessage() throws JsonProcessingException, URISyntaxException {
    ObjectMapper mapper = new ObjectMapper();
    assertEquals("{\"message\":\"message\",\"hostname\":\"trapped-speedwell-1.bigwig.lshift.net\",\"",message.sendJsonMessage("message").split("date")[0]);
  }

  @Test
  public void testRecieveJsonMessage() throws IOException {
    assertEquals("message", message.receiveJsonMessage("{\"message\":\"message\",\"hostname\":\"trapped-speedwell-1.bigwig.lshift.net\",\"date\":\"2017-06-29 15:40:24\"}").getMessage());
  }
}