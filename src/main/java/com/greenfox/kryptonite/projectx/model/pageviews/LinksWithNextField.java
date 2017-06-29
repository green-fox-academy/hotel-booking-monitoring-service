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
public class LinksWithNextField extends Links {
  public String next;
  public String last;

  public LinksWithNextField(String self, String next, String last) {
    super(self);
    this.next = next;
    this.last = last;
  }
}
