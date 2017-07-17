package com.greenfox.kryptonite.projectx.model.funnels;

import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class StepAttributes extends DataAttributes{
  private int percent;

  public StepAttributes(String path, int count, int percent) {
    super(path, count);
    this.percent = percent;
  }
}
