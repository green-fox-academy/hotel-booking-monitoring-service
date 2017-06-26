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
public class PageViewData {

  String type;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  String attributes;

  public PageViewData(String type, String attributes) {
    this.type = type;
    this.attributes = attributes;
  }
}
