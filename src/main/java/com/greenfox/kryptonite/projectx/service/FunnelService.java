package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.funnels.*;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunnelService {
  private final String url = "https://greenfox-kryptonite.herokuapp.com";

  @Autowired
  private FunnelRepository funnelRepo;

  @Autowired
  private EventToDatabase eventToDatabase;

  public long createAndSaveFunnelFormat() {
    funnelRepo.save(new Funnel());
    return funnelRepo.findOne(funnelRepo.count()).getId();
  }
}
