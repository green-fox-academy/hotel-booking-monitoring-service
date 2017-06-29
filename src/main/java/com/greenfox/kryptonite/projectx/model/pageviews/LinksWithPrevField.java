package com.greenfox.kryptonite.projectx.model.pageviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinksWithPrevField extends Links {
  String next;
  String last;
  String prev;
}
