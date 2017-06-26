package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class PageviewData {

  String type;
  long id;
  DataAttributes attributes;
}
