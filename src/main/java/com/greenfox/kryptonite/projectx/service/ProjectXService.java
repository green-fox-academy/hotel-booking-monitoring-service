package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectXService {

  private LogService logging = new LogService();
  private MessageQueueService messageQueueService = new MessageQueueService();

  public Status databaseCheck(HeartbeatRepository heartbeatRepository) throws Exception {
    if (heartbeatRepository == null) {
      logging.log("ERROR", "Database not presented.");
      logging.log("DEBUG", "Database may not exist. Check database connection or existence.");
      return new Status("ok", "error",queueCheck());
    } else if (heartbeatRepository.count() > 0) {
      logging.log("INFO",
          "Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
      return new Status("ok", "ok", queueCheck());
    } else {
      logging.log("INFO", "Database connection is ok.");
      logging.log("WARN", "Database is empty.");
      return new Status("ok", "error",queueCheck());
    }
  }

  public String queueCheck() throws Exception {
    String sentMesage = "Test";
    messageQueueService.send(sentMesage);
    String receivedMessage = messageQueueService.consume();
    if (receivedMessage.equals(sentMesage)) {
      return "ok";
    } else if (!receivedMessage.equals(sentMesage)) {
      return "error";
    } else {
      return "error";
    }
  }

  public Log endpointLogger(String pathVariable) {
    if (pathVariable.equals("/heartbeat")) {
      return logging.log("INFO", "HTTP-REQUEST=GET at " + pathVariable);
    } else {
      return logging.log("ERROR", "HTTP-ERROR at " + pathVariable);
    }
  }
}