package com.greenfox.kryptonite.projectx.model.pageviews;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity
public class HotelEventQueue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  String type;
  String path;
  String trackingId;
}
