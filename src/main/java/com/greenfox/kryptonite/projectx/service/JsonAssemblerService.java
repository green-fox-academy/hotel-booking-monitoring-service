package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;

import java.util.ArrayList;
import java.util.List;

public class JsonAssemblerService {

  public PageViewFormat returnPageView(EventToDatabaseRepository repo) {
    return new PageViewFormat(new Links("https://greenfox-kryptonite.herokuapp.com/pageviews"), returnPageViewList(repo));
  }

  public PageViewDataList returnPageViewList(EventToDatabaseRepository repo) {
    ArrayList<EventToDatabase> list = (ArrayList<EventToDatabase>) repo.findAll();
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i+1, new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return new PageViewDataList(dataList);
  }
}
