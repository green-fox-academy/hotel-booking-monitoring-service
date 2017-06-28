package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.JsonAssemblerService;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import com.greenfox.kryptonite.projectx.service.PageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public BookingStatus heartbeat() throws Exception {
    monitoringService.endpointLogger("heartbeat");
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public HotelServiceStatusList monitor() throws IOException {
    monitoringService.endpointLogger("monitor");
    return monitoringService.monitoring();
  }

  @RequestMapping(value = "/pageviews", method = RequestMethod.GET)
  public PageViewFormat pageviews(@RequestParam(name = "page", required = false) String page) throws Exception {
    int index = 0;
    monitoringService.endpointLogger("pageviews");
    pageViewService.addAttributeToDatabase(eventToDatabaseRepository);
    if(page != null){ index = Integer.parseInt(page);}
    return assembler.returnPageView(eventToDatabaseRepository, index);
  }

  @RequestMapping(value = "/{pathVariable}")
  public BookingStatus endpointLogger(@PathVariable(name = "pathVariable") String pathVariable)
      throws Exception {
    monitoringService.endpointLogger(pathVariable);
    return monitoringService.databaseCheck(heartbeatRepository);
  }
}
