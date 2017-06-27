package com.greenfox.kryptonite.projectx.model.pageviews;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class DataAttributes {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  String path;
  int count = 1;

  public DataAttributes(String path) {
    this.path = path;
  }
}
