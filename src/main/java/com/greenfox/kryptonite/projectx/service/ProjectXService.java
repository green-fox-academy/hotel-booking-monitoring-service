package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ProjectXService {
  private final Logger logger = LogManager.getLogger(ProjectXService.class);

  public Status databaseCheck(HeartbeatRepository heartbeatRepository) {
    if (heartbeatRepository == null) {
      logMessages(heartbeatRepository);
      return new Status("ok");
    } else if(heartbeatRepository.count() > 0) {
      logMessages(heartbeatRepository);
      return new Response("ok", "ok");
    } else {
      logMessages(heartbeatRepository);
      return new Response("ok", "error");
    }
  }

  public void logMessages(HeartbeatRepository heartbeatRepository) {
    if (heartbeatRepository == null) {
      System.err.println("ERROR greenfox-kryptonite.herokuapp.com " + "Database not presented.");
      System.out.println("DEBUG greenfox-kryptonite.herokuapp.com " + "Database may not exist. Check database connection or existence.");
    } else if(heartbeatRepository.count() > 0) {
      System.out.println("INFO greenfox-kryptonite.herokuapp.com " + "Database connection is ok and contains " + heartbeatRepository.count() +  " element(s).");
    } else {
      System.out.println("INFO greenfox-kryptonite.herokuapp.com " + "Database connection is ok.");
      System.out.println("WARN greenfox-kryptonite.herokuapp.com " + "Database is empty.");
    }
  }
}
