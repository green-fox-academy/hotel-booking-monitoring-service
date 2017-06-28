package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class JsonAssemblerService {

  final String HOST = "https://greenfox-kryptonite.herokuapp.com/pageviews?page=";

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page) {

    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo
        .findAllByOrderByIdAsc();

    String self = HOST + page;
    String last = HOST + (int) (Math.ceil((double) allEventList.size() / 20));
    String next = checkNextPage(self, last, page);
    String prev = checkPrevPage(page);

    return new PageViewFormat(new Links(self, next, last, prev),
        returnPageViewList(repo, page));
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> list = pagination(repo, page);
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i + 1,
          new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return dataList;
  }

  private ArrayList<EventToDatabase> pagination(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo
        .findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> finalList = new ArrayList<>();
    int endIndex = checkEndIndex(page, allEventList);

    if ((page > 0) && (allEventList.size() > 20)) {
      for (int i = page * 20 - 20; i < endIndex; ++i) {
        finalList.add(allEventList.get(i));
      }
    } else {
      finalList = allEventList;
    }
    return finalList;
  }

  private int checkEndIndex(int page, ArrayList<EventToDatabase> allEventList) {
    int endIndex = 0;
    if (allEventList.size() < page * 20) {
      endIndex = allEventList.size();
    } else {
      endIndex = page * 20;
    }
    return endIndex;
  }

  private String checkNextPage(String self, String last, int page) {
    String next;
    if (self.equals(last)) {
      next = "this is the last page";
    } else {
      next = HOST + (page + 1);
    }
    return next;
  }

  private String checkPrevPage(int page) {
    String prev;
    if (page == 1) {
      prev = "this is the first page";
    } else {
      prev = HOST + (page - 1);
    }
    return prev;
  }
}
