package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import com.greenfox.kryptonite.projectx.model.Message;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

  private LogService logging = new LogService();
  private MessageQueueService messageQueueService = new MessageQueueService();
  private Message message = new Message();

  public Status databaseCheck(HeartbeatRepository heartbeatRepository) throws Exception {
    if (heartbeatRepository == null) {
      logging.log("ERROR", "Database not presented.");
      logging.log("DEBUG", "Database may not exist. Check database connection or existence.");
      return new Status("ok", "error", queueCheck());
    } else if (heartbeatRepository.count() > 0) {
      logging.log("INFO",
          "Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
      return new Status("ok", "ok", queueCheck());
    } else {
      logging.log("INFO", "Database connection is ok.");
      logging.log("WARN", "Database is empty.");
      return new Status("ok", "error", queueCheck());
    }
  }

  public String queueCheck() throws Exception {
    String sentMessage = message.sendJsonMessage("Test");
    messageQueueService.send("Test");
    messageQueueService.consume();
    String receivedMessage = messageQueueService.getTemporaryMessage();
    System.out.println(receivedMessage);
    if (receivedMessage.equals(sentMessage)) {
      System.out.println("equals");
      return "ok";
    } else if (!receivedMessage.equals(sentMessage)) {
      System.out.println("not-equals");
      return "error";
    } else {
      System.out.println("else");
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