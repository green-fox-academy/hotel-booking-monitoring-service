package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewDataList;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.PageViewDataRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageViewService {

  @Autowired
  private PageViewDataRepository pageViewDataRepository;

  public PageViewFormat createPageViewFormat() {

    Links links = new Links("https://greenfox-kryptonite.herokuapp.com/pageviews");

    List<PageViewData> pageList = new ArrayList<>();
    for (int i = 0; i < pageViewDataRepository.count(); ++i) {
      pageList.add(pageViewDataRepository.findOne((long) i));
    }

    PageViewDataList pageViewDataList = new PageViewDataList(pageList);

    return new PageViewFormat(links, pageViewDataList);
  }
}
