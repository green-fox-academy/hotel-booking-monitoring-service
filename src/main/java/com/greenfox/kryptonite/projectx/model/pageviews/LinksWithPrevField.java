package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LinksWithPrevField extends Links {

  String next;
  String last;
  String prev;

  public LinksWithPrevField(String self, String next, String last, String prev) {
    super(self);
    this.next = next;
    this.last = last;
    this.prev = prev;
  }
}
