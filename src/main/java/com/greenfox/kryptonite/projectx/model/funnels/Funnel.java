package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "funnels")
public class Funnel {

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private long id;
}
