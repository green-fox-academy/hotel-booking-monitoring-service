package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HearthbeatRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectXService {

  public Status databaseCheck(HearthbeatRepository hearthbeatRepository) {
    if (hearthbeatRepository == null) {
      return new Status("ok");
    } else if(hearthbeatRepository.count() > 0) {
      return new Response("ok", "ok");
    } else {
      return new Response("ok", "error");
    }
  }

}
