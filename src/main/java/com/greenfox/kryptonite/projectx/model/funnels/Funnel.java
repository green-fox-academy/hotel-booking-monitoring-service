package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "funnel")
public class Funnel implements Serializable{

  @Id
  @Column(name = "id_funnel")
  @GeneratedValue (strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(mappedBy = "funnel", cascade = CascadeType.ALL)
  private List<FunnelEvent> events;
}
