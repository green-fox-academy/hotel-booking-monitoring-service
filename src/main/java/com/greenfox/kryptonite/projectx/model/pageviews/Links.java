package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Links {

  String self;
  String next;
  String last;
  String prev;

  public Links(String self) {
    this.self = self;
  }
}
