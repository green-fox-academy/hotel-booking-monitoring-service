package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.ServiceStatus;
import com.greenfox.kryptonite.projectx.model.ServiceStatusList;
import com.greenfox.kryptonite.projectx.model.Services;
import com.greenfox.kryptonite.projectx.model.Status;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
      return new Status("ok", "error", queueCheck());
    } else if (heartbeatRepository.count() > 0) {
      logger.info(
          "Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
      return new Status("ok", "ok", queueCheck());
    } else {
      logger.info("Database connection is ok.");
      logger.warn("Database is empty.");
      return new Status("ok", "error", queueCheck());
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

  public ServiceStatus monitorOtherServices(String host) {
    ServiceStatus serviceStatus;

    try {
      Status currentStatus = new RestTemplate().getForObject(host + "/heartbeat", Status.class);
      serviceStatus = new ServiceStatus(host, "ok");
    } catch (HttpServerErrorException ex) {
      serviceStatus = new ServiceStatus(host, "error");
    }
    return serviceStatus;
  }

  public ServiceStatusList monitoring() throws IOException {
    List<ServiceStatus> statuses = new ArrayList<>();
    Services services = jsonService.readFiles(DATA_PATH);
    int listSize = services.getServices().size();
    for (int i = 0; i < listSize; i++) {
      statuses.add(monitorOtherServices(services.getServices().get(i).getHost()));
    }
    return new ServiceStatusList(statuses);
  }

}