package com.greenfox.kryptonite.projectx.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.greenfox.kryptonite.projectx.model.funnels.Funnel;
import com.greenfox.kryptonite.projectx.model.funnels.FunnelEvent;
import com.greenfox.kryptonite.projectx.model.funnels.FunnelStep;
import com.greenfox.kryptonite.projectx.model.funnels.StepAttributes;
import com.greenfox.kryptonite.projectx.model.funnels.StepData;
import com.greenfox.kryptonite.projectx.model.funnels.Steps;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FunnelServiceTest {

  private FunnelRepository mockFunnelRepository;
  private EventToDatabaseRepository mockEventToDatabaseRepository;
  private FunnelEventRepository mockFunnelEventRepository;
  private List<Funnel> testList = new ArrayList<>();
  private List<Funnel> zeroList = new ArrayList<>();
  private FunnelService funnelService = new FunnelService();
  private String url = "https://greenfox-kryptonite.herokuapp.com/api/funnels/";

  @Before
  public void setUpTestEnvironment() {
    this.mockFunnelRepository = mock(FunnelRepository.class);
    this.mockEventToDatabaseRepository = mock(EventToDatabaseRepository.class);
    this.mockFunnelEventRepository = mock(FunnelEventRepository.class);
    for (int i = 0; i < 4; i++) {
      testList.add(new Funnel());
    }
  }

  @Test
  public void testCreateAndSaveFunnelFormat() {
    List<FunnelEvent> list = new ArrayList<>();
    Mockito.when(mockFunnelRepository.save(new Funnel())).thenReturn(new Funnel());
    Mockito.when(mockFunnelRepository.count()).thenReturn(4L);
    Mockito.when(mockFunnelRepository.findOne(4L)).thenReturn(new Funnel(4, list));
    assertEquals(4L, funnelService.createAndSaveFunnelFormat(mockFunnelRepository));
  }

  @Test
  public void testCreateFunnelFormatWithNullData() {
    Mockito.when(mockFunnelRepository.count()).thenReturn(4L);
    Mockito.when(mockFunnelRepository.findAll()).thenReturn(testList);
    String uri = "test";
    String expected = "FunnelFormat(pageViewLinks=PageViewLinks(self=null, next=null, prev=null, last=null, related=null), data=FunnelData(type=null, id=null, relationships=null, included=null))";
    assertEquals(expected,
        funnelService.createFunnelFormatWithNullData(4L, mockFunnelRepository).toString());
  }

  @Test
  public void testCreateFunnelFormatWithNullDataAndWithNoFunnel() {
    Mockito.when(mockFunnelRepository.count()).thenReturn(0L);
    Mockito.when(mockFunnelRepository.findAll()).thenReturn(zeroList);
    String uri = "test";
    String expected = "FunnelFormat(pageViewLinks=null, data=null)";
    assertEquals(expected,
        funnelService.createFunnelFormatWithNullData(0L, mockFunnelRepository).toString());
  }

  @Test
  public void testSaveFunnel() {
    long id = 1;
    List<EventToDatabase> eventList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      eventList.add(new EventToDatabase("testpath " + i, "testtype", 5));
    }
    List<FunnelEvent> list = new ArrayList<>();
    Funnel funnel = new Funnel(id, list);

    Mockito.when(mockEventToDatabaseRepository.findAll()).thenReturn(eventList);
    Mockito.when(mockFunnelRepository.findOne(id)).thenReturn(funnel);
    Mockito.when(mockFunnelEventRepository.save(new FunnelEvent())).thenReturn(new FunnelEvent());
    assertTrue(funnelService
        .saveFunnelEvent(id, "testpath 1", mockEventToDatabaseRepository, mockFunnelRepository,
            mockFunnelEventRepository));
  }

  @Test
  public void testSaveFunnelAssertFalse() {
    long id = 1;
    List<EventToDatabase> eventList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      eventList.add(new EventToDatabase("testpath " + i, "testtype", 5));
    }
    Mockito.when(mockEventToDatabaseRepository.findAll()).thenReturn(eventList);
    assertFalse(funnelService
        .saveFunnelEvent(id, "this will assert false", mockEventToDatabaseRepository,
            mockFunnelRepository, mockFunnelEventRepository));
  }


  @Test
  public void testGetFunnelEvents() {
    long id = 1;
    List<FunnelEvent> list = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      list.add(new FunnelEvent("testpath", 5, new Funnel()));
    }
    Mockito.when(mockFunnelRepository.findOne(id)).thenReturn(new Funnel(id, list));
    assertEquals(list, funnelService.getFunnelEvents(id, mockFunnelRepository));
  }

  @Test
  public void testCountPercentWithListSizeZero() {
    List<Steps> stepList = new ArrayList<>();
    assertEquals(10000, funnelService.countPercent(stepList, 5, (step1, step2) -> step1 * 10000 / step2));
  }

  @Test
  public void testCountPercentWithListSizeTwo() {
    List<Steps> stepList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      stepList.add(new Steps((long) i, "testtype", new StepAttributes("/testpath", 10, 0)));
    }
    assertEquals(5000, funnelService.countPercent(stepList, 5, (step1, step2) -> step1 * 10000 / step2));
  }

  @Test
  public void testCreateSelfLink() {
    String expected = "PageViewLinks(self=https://greenfox-kryptonite.herokuapp.com/api/funnels/5, next=null, prev=null, last=null, related=null)";
    assertEquals(expected, funnelService.createLink(5L, i -> new PageViewLinks(url + i)).toString());
  }

  @Test
  public void testCreateRelatedLink() {
    String expected = "PageViewLinks(self=https://greenfox-kryptonite.herokuapp.com/api/funnels/5/relationships/steps, next=null, prev=null, last=null, related=https://greenfox-kryptonite.herokuapp.com/api/funnels/5/steps)";
    assertEquals(expected, funnelService.createLink(5L, i -> new PageViewLinks(url + i + "/relationships/steps", url + i + "/steps")).toString());
  }

  @Test
  public void testCreateNewFunnelStep() {
    String expected = "FunnelStep(pageViewLinks=PageViewLinks(self=https://greenfox-kryptonite.herokuapp.com/api/funnels/5/relationships/steps, next=null, prev=null, last=null, related=https://greenfox-kryptonite.herokuapp.com/api/funnels/5/steps), data=[StepData(type=steps, id=0), StepData(type=steps, id=1), StepData(type=steps, id=2), StepData(type=steps, id=3), StepData(type=steps, id=4)])" ;
    List<StepData> stepDataList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      stepDataList.add(new StepData(i));
    }
    assertEquals(expected, funnelService.createNewFunnelStep(5L, stepDataList).toString());
  }

  @Test
  public void testCreateStepAttributes() {
    String expected = "StepAttributes(percent=125)";
    List<FunnelEvent> funnelEventList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      funnelEventList.add(new FunnelEvent("testpath", 123, new Funnel()));
    }
    assertEquals(expected, funnelService.createStepAttributes(1, funnelEventList, 125).toString());
  }

  @Test
  public void testReturnFunnelJSON () {
    String expected = "FunnelFormat(pageViewLinks=PageViewLinks(self=https://greenfox-kryptonite.herokuapp.com/api/funnels/1, next=null, prev=null, last=null, related=null), data=FunnelData(type=funnels, id=1, relationships=Relationships(funnelStep=FunnelStep(pageViewLinks=PageViewLinks(self=https://greenfox-kryptonite.herokuapp.com/api/funnels/1/relationships/steps, next=null, prev=null, last=null, related=https://greenfox-kryptonite.herokuapp.com/api/funnels/1/steps), data=[])), included=[]))";
     List<FunnelEvent> funnelEventList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      funnelEventList.add(new FunnelEvent("testpath", 5, new Funnel()));
    }
    Funnel funnel = new Funnel(1L, funnelEventList);
    Mockito.when(mockFunnelRepository.findOne(1L)).thenReturn(funnel);
    assertEquals(expected, funnelService.returnFunnelJson(1L, mockFunnelRepository).toString());
  }
}
