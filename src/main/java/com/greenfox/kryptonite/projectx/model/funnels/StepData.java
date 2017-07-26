package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StepData {

  private String type;
  private int id;

  public StepData (int id) {
    this.type = "steps";
    this.id = id;
  }
}
