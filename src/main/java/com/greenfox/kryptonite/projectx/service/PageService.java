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

  private final Integer ITEMS_PER_PAGE = 20;


  @Autowired
  EventToDatabaseRepository eventToDatabaseRepository;

  public NewPageViewFormat returnPage(Integer pageNumber, HttpServletRequest request) {
    if (pageNumber == null) {
      pageNumber = 0;
    }
    Page page = eventToDatabaseRepository.findAll(new PageRequest(pageNumber, ITEMS_PER_PAGE));
    List<EventToDatabase> list = eventToDatabaseRepository
        .findAll(new PageRequest(pageNumber, ITEMS_PER_PAGE)).getContent();
    List<PageViewData> pageViewDataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      PageViewData pageView = new PageViewData("pageviews", i,
          new DataAttributes(list.get(i).getPath(), list.get(i).getCount()));
      pageViewDataList.add(pageView);
    }
    PageViewLinks pageViewLinks = createLinks(page,request);
    return new NewPageViewFormat(pageViewLinks, pageViewDataList);
  }

  public PageViewLinks createLinks(Page page, HttpServletRequest request) {
    Integer totalPages = page.getTotalPages();
    PageViewLinks pageViewLinks = new PageViewLinks();
    String url = request.getRequestURL().toString();
    if (totalPages <= 1 || page.isFirst()) {
      pageViewLinks.setSelf(url);
    } else {
      pageViewLinks.setSelf(url + "?" + request.getQueryString());
    }

    if (page.hasNext()) {
      pageViewLinks.setNext(url + "?page=" + (page.getNumber()+1));
    }

    if (page.hasPrevious()) {
      pageViewLinks.setNext(url + "?page=" + (page.getNumber()-1));
    }

    if (page.isLast() && !page.isFirst()) {
      pageViewLinks.setLast("this is the last page");
    } else {
      pageViewLinks.setLast(url + "?page=" + totalPages);
    }
    return pageViewLinks;
  }

}
