package com.greenfox.kryptonite.projectx.model.hotelservices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelServiceStatusList {

  private List<HotelServiceStatus> statuses;

}
