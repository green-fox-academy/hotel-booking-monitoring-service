package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.funnels.*;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import com.sun.jmx.mbeanserver.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
  
  public FunnelFormat returnFunnelJson( long id) {
    List<Steps> included = new ArrayList<>();
    List<StepData> stepData = new ArrayList<>();
    for (int i = 0; i < getFunnelEvents(id).size(); i++) {
      stepData.add(new StepData(i + 1));
      included.add(new Steps(i + 1L , "steps", createStepAttributes(i, getFunnelEvents(id))));
    }
    Relationships relationships = new Relationships(createNewFunnelStep(id, stepData));
    FunnelData funnelData = new FunnelData(id, relationships, included);
    return new FunnelFormat(createSelfLink(id), funnelData);
  }

  public List<FunnelEvent> getFunnelEvents(long id) {
    return funnelRepo.findOne(id).getEvents();
  }

  public PageViewLinks createSelfLink(long id) {
    return new PageViewLinks(url + id);
  }

  public PageViewLinks createRelatedLink(long id) {
    return new PageViewLinks(url + id + "/relationships/steps",url + id + "/steps");
  }

  public FunnelStep createNewFunnelStep(long id, List<StepData> stepData) {
    return new FunnelStep(createRelatedLink(id), stepData);
  }

  public StepAttributes createStepAttributes(int i, List<FunnelEvent> events) {
    return new StepAttributes(events.get(i).getPath(), events.get(i).getCount(),10000);
  }


}
