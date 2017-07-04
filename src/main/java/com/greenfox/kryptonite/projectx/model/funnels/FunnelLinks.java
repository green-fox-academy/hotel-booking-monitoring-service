package com.greenfox.kryptonite.projectx.model.funnels;

import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class FunnelLinks extends Links {

  private String related;

  public FunnelLinks(String self, String related) {
    super(self);
    this.related = related;
  }
}
