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
@NoArgsConstructor
@Service
public class PageViewService {

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";

  MessageQueueService messageQueueService = new MessageQueueService();

  public HotelEventQueue createObjectFromJson(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, HotelEventQueue.class);
  }

  public Boolean addAttributeToDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      String hostURL, String exchangeName, String queueName, boolean bindQueue, boolean autoAck)
      throws Exception {
    Boolean checkAttributes = null;
    List<EventToDatabase> eventList = (List<EventToDatabase>) eventToDatabaseRepository.findAll();
    HotelEventQueue hotelEventQueue = consumeHotelEventQueue(hostURL, exchangeName, queueName,
        bindQueue, autoAck);

    for (int i = 0; i < messageQueueService.getCount(queueName); ++i) {
      if (eventList.size() == 0) {
        saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
        checkAttributes = false;
      } else {
        checkEventDatabase(eventToDatabaseRepository, hotelEventQueue, eventList);
        checkAttributes = true;
      }
    }
    return checkAttributes;
  }

  public HotelEventQueue consumeHotelEventQueue(String hostURL, String exchangeName,
      String queueName, boolean bindQueue, boolean autoAck) throws Exception {
    messageQueueService.consume(hostURL, exchangeName, queueName, bindQueue, autoAck);
    String temp = messageQueueService.getTemporaryMessage();
    return createObjectFromJson(temp);
  }

  public boolean checkEventDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      HotelEventQueue hotelEventQueue, List<EventToDatabase> eventList) {
    boolean checkList = false;
    for (int i = 0; i < eventToDatabaseRepository.count(); ++i) {
      if (eventList.get(i).getPath().equals(hotelEventQueue.getPath())) {
        updateEventInDatabase(eventToDatabaseRepository, eventList.get(i));
        checkList = true;
      }
    }
    if (!checkList) {
      saveEventToDatabase(eventToDatabaseRepository, hotelEventQueue);
    }
    return checkList;
  }

  public EventToDatabase saveEventToDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      HotelEventQueue hotelEventQueue) {
    EventToDatabase eventToDatabase = new EventToDatabase(hotelEventQueue.getPath(),
        hotelEventQueue.getType());
    eventToDatabaseRepository.save(eventToDatabase);
    return eventToDatabase;
  }

  public EventToDatabase updateEventInDatabase(EventToDatabaseRepository eventToDatabaseRepository,
      EventToDatabase eventToDatabase) {
    eventToDatabase.setCount(eventToDatabase.getCount() + 1);
    eventToDatabaseRepository.save(eventToDatabase);
    return eventToDatabase;
  }

  public String sendJsonHotelEventQueue() throws JsonProcessingException, URISyntaxException {
    HotelEventQueue hotelEventQueue = new HotelEventQueue("test-hotelEventQueue",
        "/testPath", "5431325134");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(hotelEventQueue);
  }

  public int returnPageIndex(String page) {
    return page != null ? Integer.parseInt(page) : 0;
  }
}
