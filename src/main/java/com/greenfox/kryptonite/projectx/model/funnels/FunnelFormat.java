package com.greenfox.kryptonite.projectx.model.funnels;


import com.greenfox.kryptonite.projectx.model.pageviews.PageViewLinks;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunnelFormat {
  private PageViewLinks pageViewLinks;
  private FunnelData data;
}
