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

  public long createAndSaveFunnelFormat(FunnelRepository funnelRepo) {
    funnelRepo.save(new Funnel());
    return funnelRepo.findOne(funnelRepo.count()).getId();
  }

  public FunnelFormat createFunnelFormatWithNullData(String uri, long id,
      FunnelRepository funnelRepo) {
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

  public boolean saveFunnelEvent(long id, String path,
      EventToDatabaseRepository eventToDatabaseRepository, FunnelRepository funnelRepo,
      FunnelEventRepository funnelEventRepository) {
    for (EventToDatabase e : eventToDatabaseRepository.findAll()) {
      if (e.getPath().equals(path)) {
        FunnelEvent funnelEvent = new FunnelEvent(e.getPath(), e.getCount(),
            funnelRepo.findOne(id));
        funnelEventRepository.save(funnelEvent);
        return true;
      }
    }
    return false;
  }

  public FunnelFormat returnFunnelJson(long id, FunnelRepository funnelRepository) {
    List<FunnelEvent> funnelEvents = new ArrayList<>();
    List<Steps> included = new ArrayList<>();
    List<StepData> stepData = new ArrayList<>();
    createStepData(id, funnelRepository, funnelEvents, included, stepData);
    Relationships relationships = new Relationships(createNewFunnelStep(id, stepData));
    FunnelData funnelData = new FunnelData(id, relationships, included);
    return new FunnelFormat(createSelfLink(id), funnelData);
  }

  private void createStepData(long id, FunnelRepository funnelRepository,
      List<FunnelEvent> funnelEvents, List<Steps> included, List<StepData> stepData) {
    for (int i = 0; i < funnelEvents.size(); i++) {
      stepData.add(new StepData(i + 1));
      int count = funnelEvents.get(i).getCount();
      included.add(new Steps(i + 1L, "steps",
          createStepAttributes(i, getFunnelEvents(id, funnelRepository),
              countPercent(included, count))));
    }
  }

  public List<FunnelEvent> getFunnelEvents(long id, FunnelRepository funnelRepo) {
    return funnelRepo.findOne(id).getEvents();
  }

  public PageViewLinks createSelfLink(long id) {
    return new PageViewLinks(url + id);
  }

  public PageViewLinks createRelatedLink(long id) {
    return new PageViewLinks(url + id + "/relationships/steps", url + id + "/steps");
  }

  public FunnelStep createNewFunnelStep(long id, List<StepData> stepData) {
    return new FunnelStep(createRelatedLink(id), stepData);
  }

  public StepAttributes createStepAttributes(int i, List<FunnelEvent> events, int percent) {
    return new StepAttributes(events.get(i).getPath(), events.get(i).getCount(), percent);
  }

  public int countPercent(List<Steps> stepList, int count) {
    CountPercentInterface countPercentInterface = (step1, step2) -> step1 * 10000 / step2;
    return stepList.size() == 0 ? 10000 : countPercentInterface
        .countPercent(count, stepList.get(stepList.size() - 1).getAttributes().getCount());
  }

  public String deleteFunnel(long id) {
    String temp = "Something went wrong";
    for (Funnel f : funnelRepo.findAll()) {
      if (f.getId() == id) {
        funnelRepo.delete(id);
        temp = "Funnel has been deleted with id: " + id;
      }
    }
    return temp;
  }
}
