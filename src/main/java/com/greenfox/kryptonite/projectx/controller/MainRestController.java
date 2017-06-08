package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.model.Status;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

  @RequestMapping(value = "/hearthbeat", method = RequestMethod.GET)
  public Status hearthBeat() {
    return new Status("ok");
  }

}
