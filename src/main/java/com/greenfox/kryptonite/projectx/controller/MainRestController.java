package com.greenfox.kryptonite.projectx.controller;


import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.NewPageViewFormat;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.JsonAssemblerService;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import com.greenfox.kryptonite.projectx.service.PageService;
import com.greenfox.kryptonite.projectx.service.PageViewService;
import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@RestController
public class MainRestController {

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private MonitoringService monitoringService;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Autowired
  PageViewService pageViewService;

  @Autowired
  private PageService pageService;

  private JsonAssemblerService assembler = new JsonAssemblerService();
  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";
  private RestTemplate restTemplate = new RestTemplate();

  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public BookingStatus heartbeat(HttpServletRequest request) throws Exception {
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/pageviews", method = RequestMethod.GET)
  public PageViewFormat pageviews(@RequestParam(name = "page", required = false) String page, @RequestParam(name = "path", required =  false) String path, @RequestParam(name = "min", required =  false) Integer min, @RequestParam(name = "max", required =  false) Integer max)
      throws Exception {
    pageViewService
        .addAttributeToDatabase(eventToDatabaseRepository, RABBIT_MQ_URL, EXCHANGE_NAME, "events",
            false, true);
    return assembler.returnPageView(eventToDatabaseRepository, pageViewService.returnPageIndex(page), path, min, max);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public HotelServiceStatusList monitor(HttpServletRequest request) throws IOException {
    return monitoringService.monitoring(restTemplate);
  }

  @RequestMapping(value = "/newpageview")
  public NewPageViewFormat listPageviews(@RequestParam(name = "page", required = false) Integer page, HttpServletRequest request) {
    System.out.println(request.getRequestURL());
    return pageService.returnPage(page,request);
  }
}
