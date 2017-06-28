package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.hotelservices.HotelService;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatus;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServices;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
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

  private static final String DATA_PATH = "./src/main/resources/monitoring-services.json";
  private Logger logger = LogManager.getLogger(this.getClass());
  private MessageQueueService messageQueueService = new MessageQueueService();
  private IOService IOService = new IOService();

  public BookingStatus databaseCheck(HeartbeatRepository heartbeatRepository) throws Exception {
    if (heartbeatRepository == null) {
      logger.error("Database not presented.");
      logger.debug("Database may not exist. Check database connection or existence.");
      return new BookingStatus("ok", "error", queueCheck());
    } else if (heartbeatRepository.count() > 0) {
      logger.info(
          "Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
      return new BookingStatus("ok", "ok", queueCheck());
    } else {
      logger.info("Database connection is ok.");
      logger.warn("Database is empty.");
      return new BookingStatus("ok", "error", queueCheck());
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
    } else if (pathVariable.equals("pageviews")) {
      logger.info("HTTP-REQUEST=GET at /" + pathVariable);
    } else {
      logger.error("HTTP-ERROR at /" + pathVariable);
    }
  }

  public HotelServiceStatus monitorOtherServices(String host, RestTemplate restTemplate) {
    HotelServiceStatus hotelServiceStatus;

    try {
      BookingStatus currentBookingStatus = restTemplate.getForObject(host + "/heartbeat", BookingStatus.class);
      hotelServiceStatus = new HotelServiceStatus(host, "ok");
    } catch (HttpServerErrorException ex) {
      hotelServiceStatus = new HotelServiceStatus(host, "error");
    }
    return hotelServiceStatus;
  }

  public HotelServiceStatusList monitoring(RestTemplate restTemplate) throws IOException {
    List<HotelServiceStatus> statuses = new ArrayList<>();
    int listSize = getHostNamesList().size();
    for (int i = 0; i < listSize; i++) {
      statuses.add(monitorOtherServices(getHostNamesList().get(i).getHost(), restTemplate));
    }
    return new HotelServiceStatusList(statuses);
  }

  public List<HotelService> getHostNamesList() throws IOException {
    HotelServices hotelServices = IOService.readFiles(DATA_PATH);
    return hotelServices.getServices();
  }

}