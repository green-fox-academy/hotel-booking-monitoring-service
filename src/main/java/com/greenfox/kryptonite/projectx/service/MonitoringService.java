package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.ServiceStatus;
import com.greenfox.kryptonite.projectx.model.ServiceStatusList;
import com.greenfox.kryptonite.projectx.model.Services;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class MonitoringService {

  private static final String DATA_PATH = "monitoring-services.json";
  private Logger logger = LogManager.getLogger(this.getClass());
  private MessageQueueService messageQueueService = new MessageQueueService();
  private JsonService jsonService = new JsonService();

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
    if (messageQueueService.getCount("heartbeat") == 0) {
      return "ok";
    } else if (messageQueueService.getCount("heartbeat") != 0) {
      return "error";
    } else {
      return "connection error";
    }
  }

  public void endpointLogger(String pathVariable) {
    if (pathVariable.equals("heartbeat")) {
      logger.info("HTTP-REQUEST=GET at /" + pathVariable);
    } else if (pathVariable.equals("monitor")) {
      logger.info("HTTP-REQUEST=GET at /" + pathVariable);
    } else {
      logger.error("HTTP-ERROR at /" + pathVariable);
    }
  }

  public ServiceStatus monitorOtherServices(String host){
    Status currentStatus = new RestTemplate().getForObject(host + "/heartbeat", Status.class);
    if (currentStatus.getStatus().equals("ok")) {
      return new ServiceStatus(host, "ok");
    } else {
      return new ServiceStatus(host, "error");
    }
  }

  public ServiceStatusList monitoring() throws IOException {
    ServiceStatusList serviceStatusList = new ServiceStatusList();
    jsonService.writeToFile(DATA_PATH);
    Services services = jsonService.readFiles(DATA_PATH);
    int listSize = services.getServices().size();
    for (int i = 0; i < listSize; i++) {
      serviceStatusList.getStatuses().add(monitorOtherServices(services.getServices().get(i).getHost()));
    }
    return serviceStatusList;
  }

}