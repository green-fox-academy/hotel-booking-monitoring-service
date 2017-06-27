package com.greenfox.kryptonite.projectx.model.pageviews;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventToDatabase {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  String path;
  String type;
  int count = 1;

  public EventToDatabase(String path, String type) {
    this.path = path;
    this.type = type;
  }
}
