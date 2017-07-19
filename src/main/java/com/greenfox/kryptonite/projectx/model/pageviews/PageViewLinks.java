package com.greenfox.kryptonite.projectx.model.pageviews;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageViewLinks {
  private String self;
  private String next;
  private String prev;
  private String last;
  private String related;

  public PageViewLinks(String self) {
    this.self = self;
  }

  public PageViewLinks(String self, String related) {
    this.self = self;
    this.related = related;
  }

  public PageViewLinks(String self, String next, String prev, String last) {
    this.self = self;
    this.next = next;
    this.prev = prev;
    this.last = last;
  }
}
