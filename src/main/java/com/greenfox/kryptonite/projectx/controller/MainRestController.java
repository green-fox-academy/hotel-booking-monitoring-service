package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.ProjectXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private ProjectXService projectXService;

  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public Status hearthbeat() {
    System.out.println("endpoint loaded");
    return projectXService.databaseCheck(heartbeatRepository);
  }
}
