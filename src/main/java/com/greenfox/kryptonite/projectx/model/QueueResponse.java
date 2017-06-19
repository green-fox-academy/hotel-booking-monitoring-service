package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueResponse extends Response {
  private String queue;

  public QueueResponse(String status, String database, String queue) {
    super(status, database);
    this.queue = queue;
  }

  public QueueResponse() {
  }
}
