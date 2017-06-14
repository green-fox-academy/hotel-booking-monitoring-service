package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectXService {
  private LogService logging = new LogService();

  public Status databaseCheck(HeartbeatRepository heartbeatRepository) {
    if (heartbeatRepository == null) {
      logging.log("ERROR","Database not presented.");
      logging.log("DEBUG","Database may not exist. Check database connection or existence.");
      return new Status ("ok");
    } else if(heartbeatRepository.count() > 0) {
      logging.log("INFO","Database connection is ok and contains " + heartbeatRepository.count() +  " element(s).");
      return new Response("ok", "ok");
    } else {
      logging.log("INFO","Database connection is ok.");
      logging.log("WARN","Database is empty.");
      return new Response("ok", "error");
    }
  }
}