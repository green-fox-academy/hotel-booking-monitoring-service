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

  MessageQueueService messageQueueService = new MessageQueueService();

  private HotelEventQueue createObjectFromJson(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, HotelEventQueue.class);
  }

  public void addAttributeToDatabase(EventToDatabaseRepository eventToDatabaseRepository)
      throws Exception {
    int times = messageQueueService.getCount("events");
    for (int i = 0; i < times; ++i) {
      HotelEventQueue hotelEventQueue = consumeHotelEventQueue();
      ArrayList<EventToDatabase> eventList = (ArrayList<EventToDatabase>) eventToDatabaseRepository
          .findAll();
      if (eventList.size() == 0) {
        saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
      } else {
        checkDatabase(eventToDatabaseRepository, hotelEventQueue, eventList);
      }
    }
  }

  private HotelEventQueue consumeHotelEventQueue() throws Exception {
    messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "events", false, true);
    String temp = messageQueueService.getTemporaryMessage();
    return createObjectFromJson(temp);
  }

  private void checkDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      HotelEventQueue hotelEventQueue, ArrayList<EventToDatabase> eventList) {
    boolean checkList = false;
    for (int i = 0; i < eventToDatabaseRepository.count(); ++i) {

      if (eventList.get(i).getPath().equals(hotelEventQueue.getPath())) {
        updateEventInDatabase(eventToDatabaseRepository, eventList.get(i));
      checkList = true;
      }
    }
    if(!checkList){
      saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
    }
  }

  private void saveEventToDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      HotelEventQueue hotelEventQueue) {
    EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(),
        hotelEventQueue.getType());
    eventToDatabaseRepository.save(eventToDatabase);
  }

  private void updateEventInDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      EventToDatabase anEventList) {
    anEventList.setCount(anEventList.getCount()+1);
    eventToDatabaseRepository.save(anEventList);
  }
}
