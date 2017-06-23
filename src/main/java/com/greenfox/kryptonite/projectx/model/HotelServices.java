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
public class HotelServices {

  List<HotelService> services;

  public HotelServices(List<HotelService> services) {
    this.services = services;
  }
}
