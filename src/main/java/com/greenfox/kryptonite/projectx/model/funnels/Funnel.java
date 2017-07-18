package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "funnels")
public class Funnel {

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(mappedBy = "funnel")
  private List<FunnelEvent> events;
}
