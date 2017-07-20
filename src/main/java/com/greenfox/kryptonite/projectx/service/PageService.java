package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PageService {

  private final Integer ITEMS_PER_PAGE = 5;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  public PageViewFormat returnPage(HttpServletRequest request, Integer pageNumber,
      Integer min, Integer max, String path) {
    pageNumber = setPageNumber(pageNumber);
    PageRequest pageRequest = new PageRequest(pageNumber,ITEMS_PER_PAGE);
    List<EventToDatabase> requestedPageViews = createListOfFilteredPageViews(pageRequest, min, max, path);
    List<PageViewData> pageViewDataList = createPageViewDataList(requestedPageViews, pageNumber);
    PageViewLinks pageViewLinks = createLinks(pageNumber, request, pageRequest);
    return new PageViewFormat(pageViewLinks, pageViewDataList);
  }

  public int setPageNumber(Integer pageNumber) {
    if (pageNumber == null || pageNumber <= 0) {
      pageNumber = 0;
    } else {
      pageNumber = pageNumber - 1;
    }
    return pageNumber;
  }

  public List<EventToDatabase> createListOfFilteredPageViews(PageRequest pageRequest, Integer min,
      Integer max, String path) {
    if (min != null || max != null || path != null) {
      return filterPageviews(min, max, path);
    } else {
      return eventToDatabaseRepository.findAll(pageRequest).getContent();
    }
  }

  public PageViewLinks createLinks(Integer pageNumber, HttpServletRequest request, PageRequest pageRequest) {
    String url = request.getRequestURL().toString();
    PageViewLinks pageViewLinks = new PageViewLinks();
    setSelf(pageViewLinks, request, url);
    if (request.getQueryString() == null || request.getQueryString().contains("page")) {
      Page page = eventToDatabaseRepository.findAll(pageRequest);
      pageNumber++;
      setLast(pageViewLinks, page, url);
      setNext(pageViewLinks, page, pageNumber, url);
      setPrevious(pageViewLinks, page, pageNumber, url);
    }
    return pageViewLinks;
  }

  public List<PageViewData> createPageViewDataList(List<EventToDatabase> list, Integer pageNumber) {
    List<PageViewData> pageViewDataList = new ArrayList<>();
    long id = (pageNumber * ITEMS_PER_PAGE) + 1;
    for (EventToDatabase event : list) {
      pageViewDataList.add(new PageViewData("pageviews", id,
          new DataAttributes(event.getPath(), event.getCount())));
      id++;
    }
    return pageViewDataList;
  }

  public void setSelf(PageViewLinks pageViewLinks, HttpServletRequest request, String url) {
    if (request.getQueryString() == null) {
      pageViewLinks.setSelf(url);
    } else {
      pageViewLinks.setSelf(url + "?" + request.getQueryString());
    }
  }

  public void setLast(PageViewLinks pageViewLinks, Page page, String url) {
    pageViewLinks.setLast(url + "?page=" + page.getTotalPages());
  }

  public void setNext(PageViewLinks pageViewLinks, Page page, Integer pageNumber, String url) {
    if (page.hasNext()) {
      pageViewLinks.setNext(url + "?page=" + (pageNumber + 1));
    } else {
      pageViewLinks.setLast("This is the last page");
    }
  }

  public void setPrevious(PageViewLinks pageViewLinks, Page page, Integer pageNumber, String url) {
    if (page.hasPrevious()) {
      pageViewLinks.setPrev(url + "?page=" + (pageNumber - 1));
    }
  }

  public List<EventToDatabase> filterPageviews(Integer min, Integer max, String path) {
    if (path != null) {
      return eventToDatabaseRepository.findAllByPath(path);
    } else if (min != null || max != null) {
      return filterPageviewsByCount(min, max);
    } else {
      return eventToDatabaseRepository.findAllByOrderByIdAsc();
    }
  }

  public ArrayList<EventToDatabase> filterPageviewsByCount(Integer min, Integer max) {
    List<EventToDatabase> pageviews = eventToDatabaseRepository.findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase pageview : pageviews) {
      if (min != null && max != null && pageview.getCount() > min && pageview.getCount() < max) {
        filteredList.add(pageview);
      } else if (min != null && max == null && pageview.getCount() > min) {
        filteredList.add(pageview);
      } else if (max != null && min == null && pageview.getCount() < max) {
        filteredList.add(pageview);
      }
    }
    return filteredList;
  }

}
