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

  List<HotelService> hotelServices;

  public HotelServices(List<HotelService> hotelServices) {
    this.hotelServices = hotelServices;
  }
}
