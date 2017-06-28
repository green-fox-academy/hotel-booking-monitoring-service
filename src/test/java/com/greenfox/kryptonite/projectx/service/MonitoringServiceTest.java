package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceTest {
  @Mock
  RestTemplate restTemplate;

  @Mock
  BookingStatus bookingStatus = mock(BookingStatus.class);

  @InjectMocks
  MonitoringService monitoringService;

  @Test
  public void testOkServiceResponseStatus() throws IOException {
    Mockito.when(restTemplate.getForObject("https://hotel-booking-resize-service.herokuapp.com/heartbeat", BookingStatus.class)).thenReturn(bookingStatus);
    assertEquals("ok", monitoringService.monitorOtherServices("https://hotel-booking-resize-service.herokuapp.com", restTemplate).getStatus());
  }

  @Test
  public void testErrorServiceResponseStatus() throws IOException {
    Mockito.when(restTemplate.getForObject("https://hotel-booking-resize-service.herokuapp.com/heartbeat", BookingStatus.class)).thenThrow(HttpServerErrorException.class);
    assertEquals("error", monitoringService.monitorOtherServices("https://hotel-booking-resize-service.herokuapp.com", restTemplate).getStatus());
  }

  @Test
  public void testMonitorOtherServices() throws Exception {
    MonitoringService monitoringService = new MonitoringService();
    assertEquals(monitoringService.monitorOtherServices("https://greenfox-kryptonite.herokuapp.com", restTemplate).getStatus(), "ok");
  }

  @Test
  public void testGetHostNameListMethod() throws IOException {
    String returnedList = "";
    for (HotelService element : monitoringService.getHostNamesList()) {
      returnedList += element.getHost() + ", ";
    }
    assertEquals("https://hotel-booking-resize-service.herokuapp.com, " +
            "https://booking-notification-service.herokuapp.com, " +
            "https://hotel-booking-user-service.herokuapp.com, " +
            "https://hotel-booking-payment.herokuapp.com, " +
            "https://booking-resource.herokuapp.com, ", returnedList);
  }

  @Test
  public void testMonitoringMethodReturnSize() throws IOException {
    assertEquals(5, monitoringService.monitoring(restTemplate).getStatuses().size());
  }
}