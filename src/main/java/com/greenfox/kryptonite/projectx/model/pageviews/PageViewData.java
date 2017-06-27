package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageViewData {

  String type;
  long id;
  DataAttributes attributes;

  public PageViewData(String type, long id, DataAttributes attributes) {
    this.type = type;
    this.id = id;
    this.attributes = attributes;
  }
}
