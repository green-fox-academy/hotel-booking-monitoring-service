package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class JsonAssemblerService {

  PaginationService paginationService = new PaginationService();
  final String PAGEVIEWHOST = "http://greenfox-kryptonite.herokuapp.com/pageviews";

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page) {
    return new PageViewFormat(createLink(repo, page),
        returnPageViewList(repo, page));
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> list = paginationService.pagination(repo, page);
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i + 1,
          new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return dataList;
  }

  Links createLink(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo
        .findAllByOrderByIdAsc();
    Links link;
    String self = paginationService.getHOST() + page;
    String last =
        paginationService.getHOST() + (int) (Math.ceil((double) allEventList.size() / 20));
    String next = paginationService.checkNextPage(self, last, page);
    String prev = paginationService.checkPrevPage(page);

    if (page == 0) {
      link = new Links(PAGEVIEWHOST);
    } else {
      link = new Links(self, next, last, prev);
    }
    return link;
  }
}
