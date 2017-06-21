package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

  private MessageQueueService messageQueueService = new MessageQueueService();
  private Logger logger = LogManager.getLogger(this.getClass());


  public Status databaseCheck(HeartbeatRepository heartbeatRepository) throws Exception {
    if (heartbeatRepository == null) {
      logger.error("Database not presented.");
      logger.debug("Database may not exist. Check database connection or existence.");
      return new Status("ok", "error",queueCheck());
    } else if (heartbeatRepository.count() > 0) {
      logger.info("Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
      return new Status("ok", "ok", queueCheck());
    } else {
      logger.info("Database connection is ok.");
      logger.warn("Database is empty.");
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

  public void endpointLogger(String pathVariable) {
    if (pathVariable.equals("heartbeat")) {
      logger.info("HTTP-REQUEST=GET at " + pathVariable);
    } else {
      logger.error( "HTTP-ERROR at " + pathVariable);
    }
  }
}