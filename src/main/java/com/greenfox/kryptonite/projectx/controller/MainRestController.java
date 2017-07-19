package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.model.funnels.FunnelFormat;
import com.greenfox.kryptonite.projectx.model.funnels.StepBody;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatusList;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.FunnelService;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import com.greenfox.kryptonite.projectx.service.PageService;
import com.greenfox.kryptonite.projectx.service.PageViewService;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
  private PageViewService pageViewService;

  @Autowired
  private FunnelService funnelService;

  @Autowired
  private PageService pageService;

  @Autowired
  private FunnelRepository funnelRepository;

  @Autowired
  private FunnelEventRepository funnelEventRepository;

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";
  private RestTemplate restTemplate = new RestTemplate();

  @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
  public BookingStatus heartbeat(HttpServletRequest request) throws Exception {
    return monitoringService.databaseCheck(heartbeatRepository);
  }

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public HotelServiceStatusList monitor(HttpServletRequest request) throws IOException {
    return monitoringService.monitoring(restTemplate);
  }

  @RequestMapping(value = "/pageviews", method = RequestMethod.GET)
  public PageViewFormat listPageviews(@RequestParam(name = "page", required = false) Integer page, HttpServletRequest request,
      @RequestParam(name = "path", required =  false) String path,
      @RequestParam(name = "min", required =  false) Integer min,
      @RequestParam(name = "max", required =  false) Integer max) throws Exception {
    pageViewService
        .addAttributeToDatabase(eventToDatabaseRepository, RABBIT_MQ_URL, EXCHANGE_NAME, "events",
            false, true);
    return pageService.returnPage(request, page, min, max, path);
  }

  @RequestMapping(value = "/api/funnels", method = RequestMethod.POST)
  public String funnelSave() {
    return "funnel has been created with id: " + funnelService.createAndSaveFunnelFormat(funnelRepository);
  }

  @RequestMapping(value = "/api/funnels/{id}", method = RequestMethod.GET)
  public FunnelFormat getNullFunnel(@PathVariable(name = "id") long id, HttpServletRequest request) {
    return funnelService.createFunnelFormatWithNullData(request.getRequestURI(), id, funnelRepository);
  }

  @RequestMapping(value = "/api/funnels/{id}/steps", method = RequestMethod.POST)
  public boolean getStepFunnel(@PathVariable(name = "id") long id, @RequestBody StepBody stepBody) {
    return funnelService.saveFunnelEvent(id, stepBody.getPath(), eventToDatabaseRepository, funnelRepository, funnelEventRepository);
  }

  @RequestMapping(value = "/api/funnels/{id}/steps", method = RequestMethod.GET)
  public FunnelFormat getNullFunnel(@PathVariable(name = "id") long id) {
    return funnelService.returnFunnelJson(id);
  }

  @RequestMapping(value = "/api/funnels/{id}/delete", method = RequestMethod.DELETE)
  public String deleteFun(@PathVariable(name = "id") long id) {
    return funnelService.deleteFunnel(id);
  }
}
