package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Response extends Status{

  private String status;
  private String database;

  public Response(String status, String database) {
    super(status);
    this.database = database;
  }

  public Response() {
  }
}
