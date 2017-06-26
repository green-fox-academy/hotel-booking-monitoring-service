package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.HotelEventQueue;
import com.greenfox.kryptonite.projectx.model.pageviews.Links;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewDataList;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewFormat;
import com.greenfox.kryptonite.projectx.repository.DataAttributesRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageViewService {

  @Autowired
  private DataAttributesRepository dataAttributesRepository;

  public HotelEventQueue createObjectFromJson(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, HotelEventQueue.class);
  }

  public void addDataAttributesObjectToDatabase(HotelEventQueue eventQueue){
    String path = eventQueue.getPath();

    DataAttributes dataAttributes = new DataAttributes(path);

    dataAttributesRepository.save(dataAttributes);
  }
}
