package com.greenfox.kryptonite.projectx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
import com.greenfox.kryptonite.projectx.model.pageviews.HotelEventQueue;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
  private String testStringDataAttributes = "not ok";
  private String testStringEventDatabaseCheck = "not ok";

  MessageQueueService messageQueueService = new MessageQueueService();

  public HotelEventQueue createObjectFromJson(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, HotelEventQueue.class);
  }

  public void addAttributeToDatabase(EventToDatabaseRepository eventToDatabaseRepository, String hostURL, String exchangeName, String queueName, boolean bindQueue, boolean autoAck)
      throws Exception {
    int times = messageQueueService.getCount(queueName);
    for (int i = 0; i < times; ++i) {
      HotelEventQueue hotelEventQueue = consumeHotelEventQueue(hostURL, exchangeName, queueName, bindQueue, autoAck);
      List<EventToDatabase> eventList = (List<EventToDatabase>) eventToDatabaseRepository.findAll();
      if (eventList.size() == 0) {
        saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
        testStringDataAttributes = "empty";
      } else {
        checkEventDatabase(eventToDatabaseRepository, hotelEventQueue, eventList);
        testStringDataAttributes = "not empty";
      }
    }
  }

  public HotelEventQueue consumeHotelEventQueue(String hostURL, String exchangeName, String queueName, boolean bindQueue, boolean autoAck) throws Exception {
    messageQueueService.consume(hostURL, exchangeName, queueName, bindQueue, autoAck);
    String temp = messageQueueService.getTemporaryMessage();
    return createObjectFromJson(temp);
  }

  public void checkEventDatabase(EventToDatabaseRepository eventToDatabaseRepository,
                                 HotelEventQueue hotelEventQueue, List<EventToDatabase> eventList) {
    boolean checkList = false;
    for (int i = 0; i < eventToDatabaseRepository.count(); ++i) {
      if (eventList.get(i).getPath().equals(hotelEventQueue.getPath())) {
        updateEventInDatabase(eventToDatabaseRepository, eventList.get(i));
      checkList = true;
      testStringEventDatabaseCheck = "paths are equals";
      }
    }
    if(!checkList){
      saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
      testStringEventDatabaseCheck = "paths are not equals";
    }
  }

  public EventToDatabase saveEventToDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      HotelEventQueue hotelEventQueue) {
    EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(), hotelEventQueue.getType());
    eventToDatabaseRepository.save(eventToDatabase);
    return eventToDatabase;
  }

  public void updateEventInDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      EventToDatabase eventToDatabase) {
    eventToDatabase.setCount(eventToDatabase.getCount()+1);
    eventToDatabaseRepository.save(eventToDatabase);
  }

  public String sendJsonHotelEventQueue() throws JsonProcessingException, URISyntaxException {
    HotelEventQueue hotelEventQueue = new HotelEventQueue("test-hotelEventQueue",
             "/testPath","5431325134");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(hotelEventQueue);
  }

}
