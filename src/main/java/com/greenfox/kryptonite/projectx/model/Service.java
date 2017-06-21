package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Service {

  String host;
  String contact;

  public Service(String host, String contact) {
    this.host = host;
    this.contact = contact;
  }
}
