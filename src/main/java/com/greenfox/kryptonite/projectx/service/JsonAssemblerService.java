package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class JsonAssemblerService {

  final String HOST = "https://greenfox-kryptonite.herokuapp.com/pageviews?page=";

  PaginationService paginationService = new PaginationService();

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page) {

    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo
        .findAllByOrderByIdAsc();

    String self = HOST + page;
    String last = HOST + (int) (Math.ceil((double) allEventList.size() / 20));
    String next = paginationService.checkNextPage(self, last, page);
    String prev = paginationService.checkPrevPage(page);

    return new PageViewFormat(new Links(self, next, last, prev),
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
}
