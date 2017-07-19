package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.funnels.*;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FunnelService {
  private final String url = "https://greenfox-kryptonite.herokuapp.com";

  @Autowired
  private FunnelRepository funnelRepo;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Autowired
  private FunnelEventRepository funnelEventRepository;


  public long createAndSaveFunnelFormat() {
    funnelRepo.save(new Funnel());
    return funnelRepo.findOne(funnelRepo.count()).getId();
  }

  public FunnelFormat createFunnelFormatWithNullData(String uri, long id) {
    Links links = new Links();
    FunnelData funnelData = new FunnelData();
    if (funnelRepo.count() == 0) {
      return new FunnelFormat();
    } else {
      Iterable<Funnel> funnelList = funnelRepo.findAll();
      for (Funnel f : funnelList) {
        if (f.getId() == id) {
          links = new Links(url + uri);
          break;
        }
      }
    }
    return new FunnelFormat(links, funnelData);
  }

  public boolean saveFunnelEvent(long id, String path) {
    for (EventToDatabase e : eventToDatabaseRepository.findAll()) {
      if (e.getPath().equals(path)) {
        FunnelEvent funnelEvent = new FunnelEvent(e.getPath(), e.getCount(), funnelRepo.findOne(id));
        funnelEventRepository.save(funnelEvent);
        return true;
      }
    }
    return false;
  }
}
