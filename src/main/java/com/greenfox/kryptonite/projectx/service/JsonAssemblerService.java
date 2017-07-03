package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithNextField;
import com.greenfox.kryptonite.projectx.model.pageviews.LinksWithPrevField;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.util.ArrayList;
import java.util.List;

public class JsonAssemblerService {

  final String PAGEVIEWHOST = "https://greenfox-kryptonite.herokuapp.com/pageviews";
  final int ITEMS_PER_PAGE = 20;
  private PaginationService paginationService = new PaginationService();

  public PageViewFormat returnPageView(EventToDatabaseRepository repo, int page, String filter,
      Integer min, Integer max) {
    return new PageViewFormat(createLink(repo, page),
        returnPageViewList(repo, page, filter, min, max));
  }

  public List<PageViewData> returnPageViewList(EventToDatabaseRepository repo, int page,
      String filter, Integer min, Integer max) {
    List<EventToDatabase> list;
    if (filter != null) {
      list = pathFilter(repo, filter);
    } else if (min != null || max != null) {
      list = minMaxFilter(repo, min, max);
    } else {
      list = paginationService.pagination(repo, page);
    }
    return createPageViewDataList(list);
  }

  public List<PageViewData> createPageViewDataList(List<EventToDatabase> list) {
    List<PageViewData> dataList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataList.add(new PageViewData(list.get(i).getType(), (long) i + 1,
          new DataAttributes(list.get(i).getPath(), list.get(i).getCount())));
    }
    return dataList;
  }

  public Links createLink(EventToDatabaseRepository repo, int page) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    String self = paginationService.getHOST() + page;
    String last =
        paginationService.getHOST() + (int) (Math
            .ceil((double) allEventList.size() / ITEMS_PER_PAGE));
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

  public ArrayList<EventToDatabase> pathFilter(EventToDatabaseRepository repo, String filter) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase event : allEventList) {
      if (event.getPath().equals(filter)) {
        filteredList.add(event);
      }
    }
    return filteredList;
  }

  public ArrayList<EventToDatabase> minMaxFilter(EventToDatabaseRepository repo, Integer min,
      Integer max) {
    List<EventToDatabase> allEventList = repo.findAllByOrderByIdAsc();
    ArrayList<EventToDatabase> filteredList = new ArrayList<>();
    for (EventToDatabase event : allEventList) {
      if (min != null && max != null) {
        if (event.getCount() > min && event.getCount() < max) {
          filteredList.add(event);
        }
      } else if (min != null) {
        if (event.getCount() > min) {
          filteredList.add(event);
        }
      } else if (max != null) {
        if (event.getCount() < max) {
          filteredList.add(event);
        }
      }
    }
    return filteredList;
  }

}
