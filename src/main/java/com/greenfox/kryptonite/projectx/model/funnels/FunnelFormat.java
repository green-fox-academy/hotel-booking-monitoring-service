package com.greenfox.kryptonite.projectx.model.funnels;


import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunnelFormat {
  private Links links;
  private DataObject data;
}
