package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

  private String status;

  public Status(String status) {
    this.status = status;
  }

  public Status() {
  }
}
