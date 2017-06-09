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
public class Hearthbeat {

  @Id
  boolean status;

  public Hearthbeat(boolean status) {
    this.status = status;
  }
}
