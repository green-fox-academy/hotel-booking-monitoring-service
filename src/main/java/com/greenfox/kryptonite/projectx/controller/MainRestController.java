package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.repository.StatusRepository;
import com.greenfox.kryptonite.projectx.service.ProjectXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

  @Autowired
  private StatusRepository statusRepo;

  @Autowired
  private ProjectXService projectXService;

  @RequestMapping(value = "/hearthbeat", method = RequestMethod.GET)
  public Object hearthBeat() {
    return projectXService.databaseCheck(statusRepo);
  }
}
