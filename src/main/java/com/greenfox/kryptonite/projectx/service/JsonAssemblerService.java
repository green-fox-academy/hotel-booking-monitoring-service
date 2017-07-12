package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithNextField;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithPrevField;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonAssemblerService {

  private final String HOST = "https://greenfox-kryptonite.herokuapp.com/pageviews?page=";
  private final int ITEMS_PER_PAGE = 20;
  private PaginationService paginationService = new PaginationService();

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page, String filter, Integer min, Integer max) {
    return new PageViewFormat(createLinkObject(repo, page), returnPageViewList(repo, page, filter, min, max));
  }

  public Links createLinkObject(EventToDatabaseRepository repo, int page) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    String self = HOST + page;
    String last = HOST + decideLastPageNumber(allEventList);
    String next = paginationService.checkNextPage(self, last, page);
    String prev = paginationService.checkPrevPage(page);
    return choseFromLinks(page, self, last, next, prev);
  }

  public int decideLastPageNumber(List<EventToDatabase> eventList) {
    if (eventList.size()%ITEMS_PER_PAGE == 0) {
      return eventList.size()/ITEMS_PER_PAGE;
    } else {
      return (eventList.size()/ITEMS_PER_PAGE) + 1;
    }
  }

  private Links choseFromLinks(int page, String self, String last, String next, String prev) {
    if (page == 0) {
      return new Links("https://greenfox-kryptonite.herokuapp.com/pageviews");
    } else if (page == 1) {
      return new LinksWithNextField(self, next, last);
    } else {
      return new LinksWithPrevField(self, next, last, prev);
    }
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page, String filter, Integer min, Integer max) {
    List<EventToDatabase> list;
    if (filter != null) {
      list = filterEventsByPath(repo, filter);
    } else if (min != null || max != null) {
      list = filterEventsByCount(repo, min, max);
    } else {
      list = paginationService.pagination(repo, page);
    }
    return createPageViewDataList(list);
  }

  public ArrayList<EventToDatabase> filterEventsByPath(EventToDatabaseRepository repo, String filter) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase event : allEventList) {
      if (event.getPath().equals(filter)) {
        filteredList.add(event);
      }
    }
    return filteredList;
  }

  public ArrayList<EventToDatabase> filterEventsByCount(EventToDatabaseRepository repo, Integer min, Integer max) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase event : allEventList) {
      if (min != null && max != null && event.getCount() > min && event.getCount() < max) {
          filteredList.add(event);
      } else if (min != null && max == null && event.getCount() > min) {
          filteredList.add(event);
      } else if (max != null && min == null && event.getCount() < max) {
          filteredList.add(event);
      }
    }
    return filteredList;
  }

  public List<PageViewData> createPageViewDataList(List<EventToDatabase> list) {
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i + 1,
          new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return dataList;
  }
}
