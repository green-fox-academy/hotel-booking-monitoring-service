package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.BookingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceTest {
  @Mock
  RestTemplate restTemplate;

  @Mock
  BookingStatus bookingStatus = mock(BookingStatus.class);

  @InjectMocks
  @Spy
  MonitoringService monitoringService;

  @Test
  public void testMockedServiceResponse() {
    Mockito.when(restTemplate.getForObject("https://hotel-booking-resize-service.herokuapp.com/heartbeat", BookingStatus.class)).thenReturn(bookingStatus);
    System.out.println(monitoringService.monitorOtherServices("https://hotel-booking-resize-service.herokuapp.com", restTemplate));
  }
}