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

  public void addAttributeToDatabase(EventToDatabaseRepository eventToDatabaseRepository) throws Exception {
    MessageQueueService messageQueueService = new MessageQueueService();
    int times = messageQueueService.getCount("events");
    for (int i = 0; i < times; ++i) {
      messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "events", false, true);

      String temp = messageQueueService.getTemporaryMessage();
      System.out.println(temp);
      HotelEventQueue hotelEventQueue = createObjectFromJson(temp);
      System.out.println(hotelEventQueue.toString());

      ArrayList<EventToDatabase> eventList = (ArrayList<EventToDatabase>) eventToDatabaseRepository
              .findAll();

      if (eventList.size() == 0) {
        EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(),
                hotelEventQueue.getType());
        System.out.println(eventToDatabase.toString());
        eventToDatabaseRepository.save(eventToDatabase);
      } else {
        for (EventToDatabase anEventList : eventList) {
          if (anEventList.getPath().equals(hotelEventQueue.getPath())) {
            int count = eventToDatabaseRepository.findOne(anEventList.getId()).getCount();
            EventToDatabase newEvent = eventToDatabaseRepository.findOne(anEventList.getId());
            newEvent.setCount(count + 1);
            System.out.println(newEvent.toString());
          } else {
            EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(),
                    hotelEventQueue.getType());
            System.out.println(eventToDatabase.toString());
            eventToDatabaseRepository.save(eventToDatabase);
          }
        }
      }
    }
  }
}
