package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServiceStatus;
import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.HotelEventQueue;
import com.greenfox.kryptonite.projectx.repository.DataAttributesRepository;
import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Service
public class PageViewService {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";

  public HotelEventQueue createObjectFromJson(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, HotelEventQueue.class);
  }

  public HotelServiceStatus addAttributeToDatabase(DataAttributesRepository dataAttributesRepository) throws Exception {

    HotelServiceStatus hotelServiceStatus = new HotelServiceStatus("test", "ok");
    MessageQueueService messageQueueService = new MessageQueueService();

    messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "events", false, false);

    String temp = messageQueueService.getTemporaryMessage();
    HotelEventQueue hotelEventQueue = createObjectFromJson(temp);
    try {
      DataAttributes dataAttributes = new DataAttributes(hotelEventQueue.getPath());
      dataAttributesRepository.save(dataAttributes);
    } catch (Exception ex) {
      hotelServiceStatus.setStatus("error");
    }
    return hotelServiceStatus;
  }
}
