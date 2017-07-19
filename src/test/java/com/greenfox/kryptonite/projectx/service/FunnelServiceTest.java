package com.greenfox.kryptonite.projectx.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.greenfox.kryptonite.projectx.model.funnels.Funnel;
import com.greenfox.kryptonite.projectx.model.funnels.FunnelEvent;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
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
    String expected = "FunnelFormat(pageViewLinks=PageViewLinks(self=null, next=null, prev=null, last=null), data=FunnelData(type=null, id=null, relationships=null, included=null))";
    assertEquals(expected,
        funnelService.createFunnelFormatWithNullData(uri, 4L, mockFunnelRepository).toString());
  }

  @Test
  public void testCreateFunnelFormatWithNullDataAndWithNoFunnel() {
    Mockito.when(mockFunnelRepository.count()).thenReturn(0L);
    Mockito.when(mockFunnelRepository.findAll()).thenReturn(zeroList);
    String uri = "test";
    String expected = "FunnelFormat(pageViewLinks=null, data=null)";
    assertEquals(expected,
        funnelService.createFunnelFormatWithNullData(uri, 0L, mockFunnelRepository).toString());
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
    assertTrue(funnelService.saveFunnelEvent(id, "testpath 1", mockEventToDatabaseRepository, mockFunnelRepository, mockFunnelEventRepository));
  }

  @Test
  public void testSaveFunnelAssertFalse() {
    long id = 1;
    List<EventToDatabase> eventList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      eventList.add(new EventToDatabase("testpath " + i, "testtype", 5));
    }
    Mockito.when(mockEventToDatabaseRepository.findAll()).thenReturn(eventList);
    assertFalse(funnelService.saveFunnelEvent(id, "this will assert false", mockEventToDatabaseRepository, mockFunnelRepository, mockFunnelEventRepository));
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
}
