package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.NewPageViewFormat;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PageService {

  private final Integer ITEMS_PER_PAGE = 5;


  @Autowired
  EventToDatabaseRepository eventToDatabaseRepository;

  public NewPageViewFormat returnPage(Integer pageNumber, HttpServletRequest request) {
    checkPageNumber(pageNumber);
    Page page = eventToDatabaseRepository.findAll(new PageRequest(pageNumber, ITEMS_PER_PAGE));
    List<EventToDatabase> list = eventToDatabaseRepository
        .findAll(new PageRequest(pageNumber, ITEMS_PER_PAGE)).getContent();
    List<PageViewData> pageViewDataList = createPageViewDataList(list,pageNumber);
    PageViewLinks pageViewLinks = createLinks(page,request);
    return new NewPageViewFormat(pageViewLinks, pageViewDataList);
  }

  public PageViewLinks createLinks(Page page, HttpServletRequest request) {
    Integer totalPages = page.getTotalPages();
    Integer pageNumber = page.getNumber() + 1;
    String url = request.getRequestURL().toString();
    PageViewLinks pageViewLinks = new PageViewLinks();

    pageViewLinks.setLast(url + "?page=" + totalPages);

    if (request.getQueryString() == null) {
      pageViewLinks.setSelf(url);
    } else {
      pageViewLinks.setSelf(url + "?" + request.getQueryString());
    }

    if (page.hasNext()) {
      pageViewLinks.setNext(url + "?page=" + (pageNumber + 1));
    } else {
      pageViewLinks.setLast("this is the last page");
    }

    if (page.hasPrevious()) {
      pageViewLinks.setPrev(url + "?page=" + (pageNumber - 1));
    }
    return pageViewLinks;
  }

  public void checkPageNumber(Integer pageNumber) {
    if (pageNumber == null) {
      pageNumber = 0;
    } else {
      pageNumber = pageNumber - 1;
    }
  }

  public List<PageViewData> createPageViewDataList(List<EventToDatabase> list, Integer pageNumber) {
    List<PageViewData> pageViewDataList = new ArrayList<>();
    long id = (pageNumber * ITEMS_PER_PAGE) + 1;
    for (EventToDatabase event : list) {
      pageViewDataList.add(new PageViewData(event.getType(), id,
          new DataAttributes(event.getPath(), event.getCount())));
      id++;
    }
    return pageViewDataList;
  }

}
