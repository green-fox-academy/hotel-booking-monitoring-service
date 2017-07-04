package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.funnels.*;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class FunnelService {
  private final String url = "https://greenfox-kryptonite.herokuapp.com";

  @Autowired
  private FunnelRepository funnelRepo;

  public FunnelFormat createAndSaveFunnelFormat(HttpServletRequest endpoint) {
    FunnelObject tempObject = new FunnelObject(new ArrayList<Steps>());
    FunnelLinks funnelLinks = new FunnelLinks("skjv", "aubcfia");
    List<StepData> stepDatas = new ArrayList<>();
    FunnelStep funnelStep = new FunnelStep(funnelLinks, stepDatas);
    funnelRepo.save(tempObject);
    return new FunnelFormat(new Links("url"), new DataObject(tempObject.getId(), new Relationships(funnelStep), tempObject.getIncluded()));
  }

}
