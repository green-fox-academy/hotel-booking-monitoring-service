package com.greenfox.kryptonite.projectx.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DatabaseStatus {

  @Id
  boolean status;

  public DatabaseStatus(boolean status) {
    this.status = status;
  }

  public DatabaseStatus() {
  }
}
