package com.greenfox.kryptonite.projectx.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HotelService {

  String host;
  String contact;

  public HotelService(String host, String contact) {
    this.host = host;
    this.contact = contact;
  }
}
