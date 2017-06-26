package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageViewData {

  long id;
  String type;
  String attributes;

  public PageViewData(String type, String attributes) {
    this.type = type;
    this.attributes = attributes;
  }
}
