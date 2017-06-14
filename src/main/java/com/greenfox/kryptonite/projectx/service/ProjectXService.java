package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectXService {

  public Status databaseCheck(HeartbeatRepository heartbeatRepository) {
    if (heartbeatRepository == null) {
      System.err.println(new Log("ERROR","Database not present.", 200));
      System.out.println(new Log("DEBUG", "Database may not exist. Check database connection or existence.", 500));
      return new Status ("ok");
    } else if(heartbeatRepository.count() > 0) {
      System.out.println(new Log("INFO", "Database connection is ok and contains " + heartbeatRepository.count() +  " element(s).", 400));
      return new Response("ok", "ok");
    } else {
      System.out.println(new Log("INFO","Database connection is ok.", 400));
      System.err.println(new Log("WARN","Database is empty.", 500));
      return new Response("ok", "error");
    }
  }
}
