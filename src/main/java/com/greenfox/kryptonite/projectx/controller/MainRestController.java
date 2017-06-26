package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.repository.PageViewDataRepository;
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
  PageViewDataRepository pageViewDataRepository;


  @GetMapping(value = "/heartbeat")
  public BookingStatus heartbeat() throws Exception {
    monitoringService.endpointLogger("heartbeat");
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @GetMapping(value = "/monitor")
  public HotelServiceStatusList monitor() throws IOException {
    return monitoringService.monitoring();
  }

  @GetMapping(value = "/pageviews")
  public PageViewFormat pageview() {
    PageViewService pageViewService = new PageViewService();
    return pageViewService.createPageViewFormat();
  }

  @GetMapping(value = "/{pathVariable}")
  public BookingStatus endpointLogger(@PathVariable(name = "pathVariable") String pathVariable)
      throws Exception {
    monitoringService.endpointLogger(pathVariable);
    return monitoringService.databaseCheck(heartbeatRepository);
  }
}
