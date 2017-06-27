package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.*;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class JsonAssemblerService {

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  public PageViewFormat returnPageView() {
    return new PageViewFormat(new Links("https://greenfox-kryptonite.herokuapp.com/pageviews"), returnPageViewList());
  }

  public PageViewDataList returnPageViewList() {
    ArrayList<EventToDatabase> list = returnDatabaseContentInListFormat(eventToDatabaseRepository);
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i+1, returnDataAttributesInString(list.get(i).getPath(), list.get(i).getCount())));
    }
    return new PageViewDataList(dataList);
  }

  public DataAttributes returnDataAttributesInString(String path, int count) {
    return new DataAttributes(path, count);
  }

  public ArrayList<EventToDatabase> returnDatabaseContentInListFormat(EventToDatabaseRepository repository) {
    ArrayList<EventToDatabase> eventList = (ArrayList<EventToDatabase>) repository.findAll();
    return eventList;
  }

}
