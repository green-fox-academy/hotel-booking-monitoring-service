package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PaginationServiceTest {

  private EventToDatabaseRepository mockRepo;
  private PaginationService paginationService;
  private ArrayList<EventToDatabase> testList = new ArrayList<>();

  @Before
  public void setUpTestEnvironment() {
    this.mockRepo = mock(EventToDatabaseRepository.class);
    this.paginationService = new PaginationService();
    for (int i = 0; i < 25; i++) {
      testList.add(new EventToDatabase("/search", "pageview", i));
    }
  }

  @Test
  public void testCheckEndIndex() {
    assertEquals(20, paginationService.checkEndIndex(1, testList));
    assertEquals(25, paginationService.checkEndIndex(2, testList));
  }

  @Test
  public void testCheckNextPage() {
    assertEquals("this is the last page", paginationService.checkNextPage("this", "this", 2));
    assertEquals("https://greenfox-kryptonite.herokuapp.com/pageviews?page=4", paginationService.checkNextPage("this is", "not equals", 3));
  }

  @Test
  public void testCheckPrevPage() {
    assertEquals("this is the first page", paginationService.checkPrevPage(1));
    assertEquals("https://greenfox-kryptonite.herokuapp.com/pageviews?page=2", paginationService.checkPrevPage(3));
  }

  @Test
  public void testPaginationMethodOnPageOne() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    ArrayList<EventToDatabase> firstTwenty = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      firstTwenty.add(testList.get(i));
    }
    assertEquals(firstTwenty, paginationService.pagination(mockRepo, 1));
  }

  @Test
  public void testPaginationMethodOnPageTwo() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    ArrayList<EventToDatabase> lastFive = new ArrayList<>();
    for (int i = 20; i < 25; i++) {
      lastFive.add(testList.get(i));
    }
    assertEquals(lastFive, paginationService.pagination(mockRepo, 2));
  }

}