package com.greenfox.kryptonite.projectx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Response {

  private String status;
  private String database;

  public Response(String status, String database) {
    this.status = status;
    this.database = database;
  }

  public Response() {
  }
}
