package com.greenfox.kryptonite.projectx.model.pageviews;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HotelEventQueue {

  String type;
  String path;
  String trackingId;
}
