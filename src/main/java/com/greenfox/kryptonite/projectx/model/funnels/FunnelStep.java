package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunnelStep {

  private StepLinks stepLinks;
  private List<StepData> data;

}
