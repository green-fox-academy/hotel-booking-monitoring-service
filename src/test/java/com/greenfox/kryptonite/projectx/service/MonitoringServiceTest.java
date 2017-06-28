package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.BookingStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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

  @Before
  public void setMockRestTemplate() throws IOException {

  }

  @Test
  public void testMockedServiceResponse() throws IOException {
//    Mockito.when(restTemplate.getForObject("https://hotel-booking-resize-service.herokuapp.com/heartbeat", BookingStatus.class)).thenThrow(HttpServerErrorException.class);
    for (int i = 0; i < monitoringService.getHostNamesList().size(); i++) {
      Mockito.when(restTemplate.getForObject(monitoringService.getHostNamesList().get(i).getHost(), BookingStatus.class)).thenThrow(HttpServerErrorException.class);
    }
    System.out.println(monitoringService.monitorOtherServices("https://booking-resource.herokuapp.com", restTemplate));
  }


  @Test
  public void testMonitorOtherServices() throws Exception {
    MonitoringService monitoringService = new MonitoringService();
    assertEquals(monitoringService.monitorOtherServices("https://greenfox-kryptonite.herokuapp.com", restTemplate).getStatus(), "ok");
  }
}