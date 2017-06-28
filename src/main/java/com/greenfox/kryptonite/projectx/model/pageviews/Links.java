package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Links {

  String self;
  String next;
  String last;
  String prev;

  public Links(String self) {
    this.self = self;
  }
}
