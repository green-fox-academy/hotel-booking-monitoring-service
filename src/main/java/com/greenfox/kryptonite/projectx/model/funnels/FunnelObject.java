package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class FunnelObject {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
  String type;
}
