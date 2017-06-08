package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectXService {

  public Status databaseCheck(StatusRepository statusRepository) {
    if(statusRepository.count() > 0) {
      return new Status("ok", "ok");
    } else {
      return new Status("ok", "error");
    }
  }
}
