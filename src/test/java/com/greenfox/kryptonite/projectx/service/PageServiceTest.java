package com.greenfox.kryptonite.projectx.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class PageServiceTest {

  private PageService pageService;
  private EventToDatabaseRepository mockRepo;
  private ArrayList<EventToDatabase> testList;

  @Before
  public void setUpTestEnvironment() {
    this.mockRepo = mock(EventToDatabaseRepository.class);
    pageService = new PageService(mockRepo);
    this.testList = new ArrayList<>();
    testList.add(new EventToDatabase("/main", "pageview", 0));
    for (int i = 1; i < 55; i++) {
      testList.add(new EventToDatabase("/search", "pageview", i));
    }
  }

  @Test
  public void createListOfFilteredPageViews_PageRequestGiven() throws Exception {
    PageRequest pageRequest = new PageRequest(3, 5);
    Page mockPage = mock(Page.class);
    Mockito.when(mockRepo.findAll(pageRequest)).thenReturn(mockPage);
    Mockito.when(mockPage.getContent()).thenReturn(testList);
    assertEquals(testList, pageService.createListOfFilteredPageViews(pageRequest, null, null, null));
  }

  @Test
  public void createListOfFilteredPageViews_PageRequestAndMinGiven() throws Exception {
    PageRequest pageRequest = new PageRequest(3, 5);
    assertEquals(pageService.filterPageviews(53,null, null),
        pageService.createListOfFilteredPageViews(pageRequest, 53, null, null));
  }

  @Test
  public void createListOfFilteredPageViews_MaxGiven() throws Exception {
    assertEquals(pageService.filterPageviews(null,2, null),
        pageService.createListOfFilteredPageViews(null, null, 2, null));
  }

  @Test
  public void createListOfFilteredPageViews_MaxAndMinGiven() throws Exception {
    assertEquals(pageService.filterPageviews(2,10, null),
        pageService.createListOfFilteredPageViews(null, 2, 10, null));
  }

  @Test
  public void createLinks() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    PageRequest pageRequest = new PageRequest(2,5);
    Page mockPage = mock(Page.class);
    Mockito.when(mockRepo.findAll(pageRequest)).thenReturn(mockPage);
    Mockito.when(mockPage.getContent()).thenReturn(testList);
    Mockito.when(mockPage.getTotalPages()).thenReturn(5);
    Mockito.when(mockPage.hasNext()).thenReturn(true);
    Mockito.when(mockPage.hasPrevious()).thenReturn(true);
    StringBuffer sb = new StringBuffer("test.com/test");
    Mockito.when(mockRequest.getRequestURL()).thenReturn(sb);
    Mockito.when(mockRequest.getQueryString()).thenReturn("page=3");
    PageViewLinks pageViewLinks = new PageViewLinks("test.com/test?page=3", "test.com/test?page=4", "test.com/test?page=2", "test.com/test?page=5");
    assertEquals(pageViewLinks.toString() ,pageService.createLinks(2, mockRequest, pageRequest).toString());
  }

  @Test
  public void createPageViewDataList() throws Exception {
  }

  @Test
  public void returnPage() throws Exception {
  }

  @Test
  public void setPageNumberTwo() throws Exception {
    assertEquals(1,pageService.setPageNumber(2));
  }

  @Test
  public void setPageNumberNull() throws Exception {
    assertEquals(0,pageService.setPageNumber(null));
  }

  @Test
  public void setPageNumberZero() throws Exception {
    assertEquals(0,pageService.setPageNumber(0));
  }

  @Test
  public void setSelf() throws Exception {
    String url = "test.com";
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    Mockito.when(mockRequest.getQueryString()).thenReturn(null);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(url, null, null, null);
    pageService.setSelf(pageViewLinks, mockRequest, url);
    assertEquals(expectedPageViewLinks.getSelf(), pageViewLinks.getSelf());
  }

  @Test
  public void setLast() throws Exception {
    String url = "test.com";
    Page mockPage = mock(Page.class);
    Mockito.when(mockPage.getTotalPages()).thenReturn(5);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(null, null, null, url + "?page=5");
    pageService.setLast(pageViewLinks, mockPage, url);
    assertEquals(expectedPageViewLinks.getLast(), pageViewLinks.getLast());
  }

  @Test
  public void setNext() throws Exception {
    String url = "test.com";
    Page mockPage = mock(Page.class);
    Mockito.when(mockPage.hasNext()).thenReturn(true);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(null, url + "?page=5", null, null);
    pageService.setNext(pageViewLinks, mockPage, 4, url);
    assertEquals(expectedPageViewLinks.getNext(), pageViewLinks.getNext());
  }

  @Test
  public void setNextNoNext() throws Exception {
    String url = "test.com";
    Page mockPage = mock(Page.class);
    Mockito.when(mockPage.hasNext()).thenReturn(false);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(null, null, null, "This is the last page");
    pageService.setNext(pageViewLinks, mockPage, 5, url);
    assertEquals(expectedPageViewLinks.getLast(), pageViewLinks.getLast());
  }

  @Test
  public void setPrevious() throws Exception {
    String url = "test.com";
    Page mockPage = mock(Page.class);
    Mockito.when(mockPage.hasPrevious()).thenReturn(true);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(null, null, url + "?page=4", null);
    pageService.setPrevious(pageViewLinks, mockPage, 5, url);
    assertEquals(expectedPageViewLinks.getPrev(), pageViewLinks.getPrev());
  }

  @Test
  public void setPreviousNoPrev() throws Exception {
    String url = "test.com";
    Page mockPage = mock(Page.class);
    Mockito.when(mockPage.hasPrevious()).thenReturn(false);
    PageViewLinks pageViewLinks = new PageViewLinks();
    PageViewLinks expectedPageViewLinks = new PageViewLinks(null, null, null, null);
    pageService.setPrevious(pageViewLinks, mockPage, 1, url);
    assertEquals(expectedPageViewLinks.getPrev(), pageViewLinks.getPrev());
  }

  @Test
  public void filterPageviews_WithPath() throws Exception {
    Mockito.when(mockRepo.findAllByPath("/main")).thenReturn(new ArrayList<>(Arrays.asList(new EventToDatabase("/main","pageviews", 0))));
    assertEquals(new ArrayList<>(Arrays.asList(new EventToDatabase("/main","pageviews", 0))).toString(), pageService.filterPageviews(null, null, "/main").toString());
  }

  @Test
  public void filterPageviews_WithMin() throws Exception {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/search", "pageview", 53), new EventToDatabase("/search", "pageview", 54)))).toString(), pageService.filterPageviews(52,null,null).toString());
  }

  @Test
  public void filterPageviews_WithMax() throws Exception {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/main", "pageview", 0), new EventToDatabase("/search", "pageview", 1)))).toString(), pageService.filterPageviews(null,2, null).toString());
  }

  @Test
  public void filterPageviews_WithMaxAndMax() throws Exception {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/search", "pageview", 6), new EventToDatabase("/search", "pageview", 7)))).toString(), pageService.filterPageviews(5,8, null).toString());
  }

  @Test
  public void filterPageviews_WithNulls() throws Exception {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(testList, pageService.filterPageviews(null,null, null));
  }

  @Test
  public void testFilterPageviewsByCountBothParam() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(2, pageService.filterPageviewsByCount(5,8).size());
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/search", "pageview", 6), new EventToDatabase("/search", "pageview", 7)))).toString(), pageService.filterPageviewsByCount(5,8).toString());
  }

  @Test
  public void tesFilterPageviewsByCountWithMin() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(2, pageService.filterPageviewsByCount(52,null).size());
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/search", "pageview", 53), new EventToDatabase("/search", "pageview", 54)))).toString(), pageService.filterPageviewsByCount(52,null).toString());
  }

  @Test
  public void testFilterPageviewsByCountWithMax() {
    Mockito.when(mockRepo.findAllByOrderByIdAsc()).thenReturn(testList);
    assertEquals(2, pageService.filterPageviewsByCount(null,2).size());
    assertEquals(new ArrayList<>((Arrays.asList(new EventToDatabase("/main", "pageview", 0), new EventToDatabase("/search", "pageview", 1)))).toString(), pageService.filterPageviewsByCount(null,2).toString());
  }

}