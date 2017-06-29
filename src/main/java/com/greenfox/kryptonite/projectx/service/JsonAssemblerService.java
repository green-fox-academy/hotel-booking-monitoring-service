package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;

public class JsonAssemblerService {

  PaginationService paginationService = new PaginationService();
  final String PAGEVIEWHOST = "http://greenfox-kryptonite.herokuapp.com/pageviews";

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page, String filter) {
    return new PageViewFormat(createLink(repo, page),
        returnPageViewList(repo, page, filter));
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page, String filter) {
    ArrayList<EventToDatabase> list;
    if (filter.equals("")) {
      list = paginationService.pagination(repo, page);
    } else {
      list = filter(repo,filter);
    }

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
    String self = paginationService.getHOST() + page;
    String last =
        paginationService.getHOST() + (int) (Math.ceil((double) allEventList.size() / 20));
    String next = paginationService.checkNextPage(self, last, page);
    String prev = paginationService.checkPrevPage(page);

    if (page == 0) {
      return new Links(PAGEVIEWHOST);
    } else if (page == 1) {
      return new LinksWithNextField(self, next, last);
    } else {
      return new LinksWithPrevField(self, next, last, prev);
    }
  }

  private ArrayList<EventToDatabase> filter(EventToDatabaseRepository repo, String filter) {
    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo
        .findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase anAllEventList : allEventList) {
      if (anAllEventList.getPath().equals(filter)) {
        filteredList.add(anAllEventList);
      }
    }
    return filteredList;
  }

}
