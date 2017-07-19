package com.greenfox.kryptonite.projectx.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.greenfox.kryptonite.projectx.model.funnels.Funnel;
import com.greenfox.kryptonite.projectx.model.funnels.FunnelEvent;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FunnelServiceTest {

  private FunnelRepository mockFunnelRepository;
  private List<Funnel> testList = new ArrayList<>();

  private FunnelService funnelService = new FunnelService();



  @Before
  public void setUpTestEnvironment() {
    this.mockFunnelRepository = mock(FunnelRepository.class);
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
    String expected = "FunnelFormat(links=Links(self=null), data=FunnelData(type=null, id=null, relationships=null, included=null))";
    assertEquals(expected, funnelService.createFunnelFormatWithNullData(uri, 4L, mockFunnelRepository).toString());

  }
}
