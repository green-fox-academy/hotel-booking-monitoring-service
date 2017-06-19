package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

  private String status;
  private String database;
  private String queue;

  public Status(String status, String database, String queue) {
    this.status = status;
    this.database = database;
    this.queue = queue;
  }

  public Status() {
  }
}
