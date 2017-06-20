package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.service.MessageQueueService;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainRestController {

  Logger logger = LogManager.getLogger(this.getClass());

  MessageQueueService messageQueueService = new MessageQueueService();

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private MonitoringService monitoringService;

  
  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public Status heartbeat() throws Exception {
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/{endpointName}", method = RequestMethod.GET)
  public void endPointLog(@PathVariable(name = "endpointName")String path) {
    monitoringService.endpointLogger(path);
  }

}
