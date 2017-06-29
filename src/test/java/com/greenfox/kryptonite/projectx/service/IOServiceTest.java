package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IOServiceTest {

  private static final String DATAPATH = "./src/test/resources/test-monitoring-services.json";

  @Test
  public void testWriteFile() throws JsonProcessingException {
    IOService IOService = new IOService();
    assertTrue(IOService.writeToFile(DATAPATH));
  }

  @Test
  public void testReadFile() throws IOException {
    IOService IOService = new IOService();

    ObjectMapper mapper = new ObjectMapper();
    String readJson = mapper.writeValueAsString(IOService.readFiles(DATAPATH));
    String expected = "{\"services\":[{\"host\":\"https://hotel-booking-resize-service.herokuapp.com\",\"contact\":\"berta@greenfox.com\"},{\"host\":\"https://booking-notification-service.herokuapp.com\",\"contact\":\"tojasmamusza@greenfox.com\"},{\"host\":\"https://hotel-booking-user-service.herokuapp.com\",\"contact\":\"imi@greenfox.com\"},{\"host\":\"https://hotel-booking-payment.herokuapp.com\",\"contact\":\"yesyo@greenfox.com\"},{\"host\":\"https://booking-resource.herokuapp.com\",\"contact\":\"MrPoopyButthole@podi.com\"}]}";
    assertEquals(expected, readJson);
  }
}