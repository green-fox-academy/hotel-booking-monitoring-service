package com.greenfox.kryptonite.projectx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String status;
  private String datebase;

  public Status(String status, String datebase) {
    this.status = status;
    this.datebase = datebase;
  }

  public Status() {
  }
}
