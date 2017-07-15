package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelService;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IOServiceTest {

  private static final String DATAPATH = "./src/test/resources/test-monitoring-services.json";
  IOService ioService = new IOService();

  @Test
  public void testWriteFile() throws JsonProcessingException {
    assertTrue(ioService.writeToFile(DATAPATH));
  }

  @Test
  public void testReadFile() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String readJson = mapper.writeValueAsString(ioService.readFiles(DATAPATH));
    String expected = "{\"services\":[{\"host\":\"https://hotel-booking-resize-service.herokuapp.com\",\"contact\":\"berta@greenfox.com\"},{\"host\":\"https://booking-notification-service.herokuapp.com\",\"contact\":\"tojasmamusza@greenfox.com\"},{\"host\":\"https://hotel-booking-user-service.herokuapp.com\",\"contact\":\"imi@greenfox.com\"},{\"host\":\"https://hotel-booking-payment.herokuapp.com\",\"contact\":\"yesyo@greenfox.com\"},{\"host\":\"https://booking-resource.herokuapp.com\",\"contact\":\"MrPoopyButthole@podi.com\"}]}";
    assertEquals(expected, readJson);
  }

  @Test
  public void testReadFileWithBadString() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String readJson = mapper.writeValueAsString(ioService.readFiles(DATAPATH));
    String expected = "this is a bad string";
    assertNotEquals(expected, readJson);
  }

  @Test
  public void testAddContactsToWrite() {
    HotelService resize = new HotelService("https://hotel-booking-resize-service.herokuapp.com",
        "berta@greenfox.com");
    HotelService notification = new HotelService(
        "https://booking-notification-service.herokuapp.com", "tojasmamusza@greenfox.com");
    HotelService userSerice = new HotelService("https://hotel-booking-user-service.herokuapp.com",
        "imi@greenfox.com");
    HotelService payment = new HotelService("https://hotel-booking-payment.herokuapp.com",
        "yesyo@greenfox.com");
    HotelService resource = new HotelService("https://booking-resource.herokuapp.com",
        "MrPoopyButthole@podi.com");
    List<HotelService> hotelServiceList = new ArrayList<>(
        Arrays.asList(resize, notification, userSerice, payment, resource));

    assertEquals(ioService.addContactsToWrite().toString(),
        new HotelServices(hotelServiceList).toString());
  }

  @Test
  public void testAddContactWithNull() {
    assertNotEquals(ioService.addContactsToWrite(), null);
  }
}