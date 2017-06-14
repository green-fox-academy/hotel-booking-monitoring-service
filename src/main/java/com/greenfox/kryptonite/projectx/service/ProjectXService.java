package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class ProjectXService {

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
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    if (heartbeatRepository == null) {
      System.err.println("ERROR " + date + " greenfox-kryptonite.herokuapp.com " + "Database not presented.");
      System.out.println("DEBUG " + date + " greenfox-kryptonite.herokuapp.com " + "Database may not exist. Check database connection or existence.");
    } else if(heartbeatRepository.count() > 0) {
      System.out.println("INFO " + date + " greenfox-kryptonite.herokuapp.com " + "Database connection is ok and contains " + heartbeatRepository.count() +  " element(s).");
    } else {
      System.out.println("INFO " + date + " greenfox-kryptonite.herokuapp.com " + "Database connection is ok.");
      System.out.println("WARN " + date + " greenfox-kryptonite.herokuapp.com " + "Database is empty.");
    }
  }
}
