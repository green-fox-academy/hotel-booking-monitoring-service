package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FunnelEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String path;
  private int count;
  @ManyToOne
  @JoinColumn (name = "id_funnel")
  private Funnel funnel;
}
