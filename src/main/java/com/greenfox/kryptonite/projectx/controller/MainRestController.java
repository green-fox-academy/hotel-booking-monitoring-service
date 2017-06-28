package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.JsonAssemblerService;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import com.greenfox.kryptonite.projectx.service.PageViewService;
import javax.servlet.http.HttpServletRequest;
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
  public BookingStatus heartbeat(HttpServletRequest request) throws Exception {
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/pageviews", method = RequestMethod.GET)
  public PageViewFormat pageviews() throws Exception {
    monitoringService.endpointLogger("pageviews");
    pageViewService.addAttributeToDatabase(eventToDatabaseRepository);
    return assembler.returnPageView(eventToDatabaseRepository);
  }

  public HotelServiceStatusList monitor(HttpServletRequest request) throws IOException{
  return monitoringService.monitoring();
  }
}
