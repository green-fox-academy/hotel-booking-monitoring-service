package com.greenfox.kryptonite.projectx.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Services {

  List<Service> services;

  public Services(List<Service> services) {
    this.services = services;
  }
}
