package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class JsonAssemblerServiceTest {

  private final String HOSTNAME= "https://greenfox-kryptonite.herokuapp.com/pageviews";
  private EventToDatabaseRepository mockRepo;
  private ArrayList<EventToDatabase> testList;
  private JsonAssemblerService assembler;

  @Before
  public void setUpTestEnvironment() {
    this.mockRepo = mock(EventToDatabaseRepository.class);
    this.testList = new ArrayList<>();
    this.assembler = new JsonAssemblerService();
    for (int i = 0; i < 55; i++) {
      testList.add(new EventToDatabase("/search", "pageview", i));
    }
  }

  @Test
  public void testCreateLink() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(HOSTNAME, assembler.createLink(mockRepo, 0).getSelf());
    assertEquals(HOSTNAME + "?page=1", assembler.createLink(mockRepo, 1).getSelf());
    assertEquals(HOSTNAME + "?page=2", ((LinksWithNextField) assembler.createLink(mockRepo, 1)).getNext());
    assertEquals(HOSTNAME + "?page=3", ((LinksWithNextField) assembler.createLink(mockRepo, 1)).getLast());
    assertEquals(HOSTNAME + "?page=2", assembler.createLink(mockRepo, 2).getSelf());
    assertEquals(HOSTNAME + "?page=3", ((LinksWithPrevField) assembler.createLink(mockRepo, 2)).getNext());
    assertEquals(HOSTNAME + "?page=3", ((LinksWithPrevField) assembler.createLink(mockRepo, 2)).getLast());
    assertEquals(HOSTNAME + "?page=1", ((LinksWithPrevField) assembler.createLink(mockRepo, 2)).getPrev());
    assertEquals("this is the last page", ((LinksWithPrevField) assembler.createLink(mockRepo, 3)).getNext());
  }

  @Test
  public void testReturnPageViewList() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    List<PageViewData> finalList = new ArrayList<>();
    for (int i = 20; i < 40; i++) {
      finalList.add(new PageViewData(testList.get(i).getType(), (long) (i-20) + 1,
              new DataAttributes(testList.get(i).getPath(), testList.get(i).getCount())));
    }
    assertEquals(finalList.size() ,assembler.returnPageViewList(mockRepo, 2, null, null, null).size());
    assertEquals(finalList.get(5).getId(), assembler.returnPageViewList(mockRepo, 2, null, null, null).get(5).getId());
    assertEquals(finalList.get(5).getAttributes().getCount(), assembler.returnPageViewList(mockRepo, 2, null, null, null).get(5).getAttributes().getCount());
  }

  @Test
  public void testReturnPageView() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(20, assembler.returnPageView(mockRepo, 2, null, null, null).getData().size());
    assertEquals(15, assembler.returnPageView(mockRepo, 3, null, null, null).getData().size());
    assertEquals(HOSTNAME + "?page=2",assembler.returnPageView(mockRepo, 2, null, null, null).getLinks().getSelf());
    assertEquals(25, assembler.returnPageView(mockRepo, 2, null, null, null).getData().get(5).getAttributes().getCount());
  }
}