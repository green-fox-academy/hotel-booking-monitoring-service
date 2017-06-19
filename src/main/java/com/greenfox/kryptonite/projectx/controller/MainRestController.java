package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.Log;
import com.greenfox.kryptonite.projectx.service.MessageQueueService;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.ProjectXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

  MessageQueueService messageQueueService = new MessageQueueService();

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private ProjectXService projectXService;


  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public Status heartbeat() throws Exception {
    projectXService.endpointLogger("heartbeat");
    return projectXService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/{endpointName}", method = RequestMethod.GET)
  public Log endPointLog(@PathVariable(name = "endpointName")String path) {
    projectXService.endpointLogger(path);
    return projectXService.endpointLogger(path);
  }

}
