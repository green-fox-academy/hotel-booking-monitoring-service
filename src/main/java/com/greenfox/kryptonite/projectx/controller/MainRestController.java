package com.greenfox.kryptonite.projectx.controller;



import com.greenfox.kryptonite.projectx.model.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
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


  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public BookingStatus heartbeat(HttpServletRequest request) throws Exception {
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public HotelServiceStatusList monitor(HttpServletRequest request) throws IOException{
  return monitoringService.monitoring();
  }

}
