package com.greenfox.kryptonite.projectx.controller;



import com.greenfox.kryptonite.projectx.model.ServiceStatusList;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
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
  public Status heartbeat() throws Exception {
    monitoringService.endpointLogger("heartbeat");
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public ServiceStatusList monitor() throws IOException{
  return monitoringService.monitoring();
  }

  @RequestMapping(value = "/{pathVariable}")
  public Status endpointLogger(@PathVariable(name = "pathVariable") String pathVariable) throws Exception {
    monitoringService.endpointLogger(pathVariable);
    return monitoringService.databaseCheck(heartbeatRepository);
  }
}
