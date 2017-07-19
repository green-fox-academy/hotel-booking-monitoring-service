package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.funnels.*;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class FunnelService {
  private final String url = "https://greenfox-kryptonite.herokuapp.com/api/funnels/";

  @Autowired
  private FunnelRepository funnelRepo;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Autowired
  private FunnelEventRepository funnelEventRepository;

  @Autowired


  public long createAndSaveFunnelFormat(FunnelRepository funnelRepo) {
    funnelRepo.save(new Funnel());
    return funnelRepo.findOne(funnelRepo.count()).getId();
  }


  public FunnelFormat createFunnelFormatWithNullData(String uri, long id, FunnelRepository funnelRepo) {
    PageViewLinks pageViewLinks = new PageViewLinks();
    FunnelData funnelData = new FunnelData();
    if (funnelRepo.count() == 0) {
      return new FunnelFormat();
    } else {
      Iterable<Funnel> funnelList = funnelRepo.findAll();
      for (Funnel f : funnelList) {
        if (f.getId() == id) {
          pageViewLinks.setSelf(url + uri);
          break;
        }
      }
    }
    return new FunnelFormat(pageViewLinks, funnelData);
  }

  public boolean saveFunnelEvent(long id, String path, EventToDatabaseRepository eventToDatabaseRepository, FunnelRepository funnelRepo, FunnelEventRepository funnelEventRepository) {
    for (EventToDatabase e : eventToDatabaseRepository.findAll()) {
      if (e.getPath().equals(path)) {
        FunnelEvent funnelEvent = new FunnelEvent(e.getPath(), e.getCount(), funnelRepo.findOne(id));
        funnelEventRepository.save(funnelEvent);
        return true;
      }
    }
    return false;
  }

  public List<FunnelEvent> getFunnelEvents(long id, FunnelRepository funnelRepo) {
    return funnelRepo.findOne(id).getEvents();
  }

  public FunnelFormat returnFunnelJson( long id, FunnelRepository funnelRepo) {
    List<FunnelEvent> events = getFunnelEvents(id, funnelRepo);
    List<Steps> included = new ArrayList<>();
    List<StepData> stepDatas = new ArrayList<>();
    for (int i = 0; i < events.size(); i++) {
      StepData stepData = new StepData(i + 1);
      stepDatas.add(stepData);
      StepAttributes stepAttributes = new StepAttributes(events.get(i).getPath(), events.get(i).getCount(), 10000);
      included.add(new Steps(i + 1L , "steps", stepAttributes));
    }
    PageViewLinks pageViewLinks = new PageViewLinks(url + id + "/relationships/steps", null, null, null, url + id + "/steps");
    FunnelStep funnelStep = new FunnelStep(pageViewLinks, stepDatas);
    Relationships relationships = new Relationships(funnelStep);
    FunnelData funnelData = new FunnelData(id, relationships, included);
    PageViewLinks funnelSelfLink = new PageViewLinks(url + id, null, null, null, null);
    return new FunnelFormat(funnelSelfLink, funnelData);
  }

  public String deleteFunnel(long id) {
    String temp = "not working";
    for(Funnel f : funnelRepo.findAll()) {
      if (f.getId()==id) {
        funnelRepo.delete(id);
        temp = "deleted funnel with id: " + id;
      }
    }
    return temp;
  }

}
