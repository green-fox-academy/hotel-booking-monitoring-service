package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatus;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.JsonAssemblerService;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import com.greenfox.kryptonite.projectx.service.PageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@RestController
public class MainRestController {

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private MonitoringService monitoringService;


  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Autowired
  PageViewService pageViewService;

  private JsonAssemblerService assembler = new JsonAssemblerService();
  private RestTemplate restTemplate= new RestTemplate();


  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public BookingStatus heartbeat() throws Exception {
    monitoringService.endpointLogger("heartbeat");
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public HotelServiceStatusList monitor() throws IOException {
    monitoringService.endpointLogger("monitor");
    return monitoringService.monitoring(restTemplate);
  }

  @RequestMapping(value = "/pageviews", method = RequestMethod.GET)
  public PageViewFormat pageviews() throws Exception {
    monitoringService.endpointLogger("pageviews");
    pageViewService.addAttributeToDatabase(eventToDatabaseRepository);
    return assembler.returnPageView(eventToDatabaseRepository);
  }

  @RequestMapping(value = "/{pathVariable}")
  public BookingStatus endpointLogger(@PathVariable(name = "pathVariable") String pathVariable)
      throws Exception {
    monitoringService.endpointLogger(pathVariable);
    return monitoringService.databaseCheck(heartbeatRepository);
  }
}
