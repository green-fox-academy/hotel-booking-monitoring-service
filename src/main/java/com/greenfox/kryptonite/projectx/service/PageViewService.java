package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewDataList;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.PageViewDataRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class PageViewService {

  @Autowired
  private
  PageViewDataRepository pageViewDataRepository;

  public PageViewFormat createPageViewFormat(String link) {

    Links links = new Links(link);


    List<PageViewData> pageList = new ArrayList<>();
    for (int i = 0; i < pageViewDataRepository.count(); ++i) {
      pageList.add(pageViewDataRepository.findOne((long) i));
    }

    PageViewDataList pageViewDataList = new PageViewDataList(pageList);

    return new PageViewFormat(links, pageViewDataList);
  }
}
