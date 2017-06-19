package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.Log;
import com.greenfox.kryptonite.projectx.model.Send;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.ProjectXService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MainRestController {

  Send send = new Send();

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private ProjectXService projectXService;

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
  public Log errorHandling (NotFoundException e) {
    System.out.println(e.getLocalizedMessage());
    System.out.println(e.getMessage());
    projectXService.endpointLogger(e.getMessage());
    return projectXService.endpointLogger(e.getMessage());
  }


  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public Status heartbeat() throws Exception {
    send.send();
    send.consume();
    return projectXService.databaseCheck(heartbeatRepository);
  }


}
