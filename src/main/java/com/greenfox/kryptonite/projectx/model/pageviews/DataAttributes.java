package com.greenfox.kryptonite.projectx.model.pageviews;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataAttributes {

  String path;
  int count;

  public DataAttributes(String path, int count) {
    this.path = path;
    this.count = count;
  }
}
