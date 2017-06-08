package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

  @Autowired
  private StatusRepository statusRepo;

  @RequestMapping(value = "/hearthbeat", method = RequestMethod.GET)
  public Status hearthBeat() {
    if(statusRepo.count() > 0) {
      return new Status("ok", "ok");
    } else {
      return new Status("ok", "error");
    }
  }
}
