package com.greenfox.kryptonite.projectx.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Heartbeat {

  @Id
  boolean status;

  public Heartbeat(boolean status) {
    this.status = status;
  }
}
