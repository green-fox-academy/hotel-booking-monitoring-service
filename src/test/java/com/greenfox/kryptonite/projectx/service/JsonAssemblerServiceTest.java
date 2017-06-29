package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithNextField;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithPrevField;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

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

}