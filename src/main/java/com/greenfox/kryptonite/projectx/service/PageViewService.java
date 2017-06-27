package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.HotelEventQueue;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.io.IOException;
import java.util.ArrayList;
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

  public void addAttributeToDatabase(
      EventToDatabaseRepository eventToDatabaseRepository) throws Exception {
    MessageQueueService messageQueueService = new MessageQueueService();
    for (int i = 0; i < messageQueueService.getCount("events"); ++i) {
      messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "events", false, true);

      String temp = messageQueueService.getTemporaryMessage();
      HotelEventQueue hotelEventQueue = createObjectFromJson(temp);

      ArrayList<EventToDatabase> eventList = (ArrayList<EventToDatabase>) eventToDatabaseRepository
          .findAll();

      for (EventToDatabase anEventList : eventList) {
        if (anEventList.getPath().equals(hotelEventQueue.getPath())) {
          int count = eventToDatabaseRepository.findOne(anEventList.getId()).getCount();
          eventToDatabaseRepository.findOne(anEventList.getId()).setCount(count + 1);
        } else {
          EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(),
              hotelEventQueue.getType());
          eventToDatabaseRepository.save(eventToDatabase);
        }
      }
    }
  }
}
