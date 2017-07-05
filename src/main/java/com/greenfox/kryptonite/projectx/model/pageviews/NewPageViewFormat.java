package com.greenfox.kryptonite.projectx.model.pageviews;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPageViewFormat {
  private PageViewLinks links;
  private List<PageViewData> data;
}
