package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class JsonAssemblerService {

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page) {
    return new PageViewFormat(new Links("https://greenfox-kryptonite.herokuapp.com/pageviews"), returnPageViewList(repo, page));
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page) {
    ArrayList<EventToDatabase> list = pagination(repo, page);
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i+1, new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return dataList;
  }

  private ArrayList<EventToDatabase> pagination(EventToDatabaseRepository repo, int page){
    ArrayList<EventToDatabase> allEventList = (ArrayList<EventToDatabase>) repo.findAllByOrderByIdAsc();

    ArrayList<EventToDatabase> finalList = new ArrayList<>();
    if(page == 0){
      finalList = allEventList;
    } else {
      for (int i = page - 1; i < page + 18; ++i) {
        finalList.add(allEventList.get(i));
      }
    }
    return finalList;
  }
}
