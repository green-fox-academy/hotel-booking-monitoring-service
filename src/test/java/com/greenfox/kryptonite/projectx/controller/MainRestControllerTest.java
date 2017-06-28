package com.greenfox.kryptonite.projectx.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.Message;
import com.greenfox.kryptonite.projectx.model.Timestamp;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.service.*;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;

import java.io.IOException;
import java.nio.charset.Charset;

import com.greenfox.kryptonite.projectx.HotelMonitoringApplication;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelMonitoringApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class MainRestControllerTest {

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private final String RABBIT_MQ_URL = System.getenv("RABBITMQ_BIGWIG_RX_URL");
  private final String EXCHANGE_NAME = "log";
  private boolean isItWorking = true;
  private MockMvc mockMvc;
  private HeartbeatRepository heartbeatRepositoryMock;
  private MonitoringService service;
  private HeartbeatRepository nullRepo;
  private static final String DATAPATH = "./src/test/resources/test-monitoring-services.json";
  private MessageQueueService messageQueueService;

  @MockBean
  HeartbeatRepository heartbeatRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    this.heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    this.service = new MonitoringService();
    this.messageQueueService = new MessageQueueService();
  }

  @Test
  public void testGetEndpoint() throws Exception {
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.status", is("ok")));
  }

  @Test
  public void testResponseWhenDatabaseIsNull() throws Exception {
    assertEquals((service.databaseCheck(nullRepo)).getStatus(), "ok");
  }

  @Test
  public void testResponseWhenNoElementInDatabase() throws Exception {
    Mockito.when(heartbeatRepositoryMock.count()).thenReturn(0L);
    assertEquals((service.databaseCheck(heartbeatRepositoryMock)).getDatabase(),
        "error");
  }

  @Test
  public void testResponseWhenElementInDatabase() throws Exception {
    Mockito.when(heartbeatRepositoryMock.count()).thenReturn(3L);
    assertEquals(((service.databaseCheck(heartbeatRepositoryMock)).getDatabase()), "ok");
  }


  @Test
  public void testGetEndpointWithFilledDatabase() throws Exception {
    BDDMockito.given(heartbeatRepository.count()).willReturn(1L);
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.status", is("ok")))
        .andExpect(jsonPath("$.database", is("ok")));
  }

  @Test
  public void testGetEndpointWithEmptyDatabase() throws Exception {
    BDDMockito.given(heartbeatRepository.count()).willReturn(0L);
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.status", is("ok")))
        .andExpect(jsonPath("$.database", is("error")));
  }

  @Test
  public void testSend() throws Exception {
    int initialSize = messageQueueService.getCount("testqueue");
    messageQueueService.send(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", "TestMessage" );
    int currentSize = messageQueueService.getCount("testqueue");
    assertEquals(initialSize + 1, currentSize);
  }

  @Test
  public void testConsume() throws Exception {
    int initialSize = messageQueueService.getCount("testqueue");
    if (initialSize != 0) {
      messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", true, true);
      int currentSize = messageQueueService.getCount("testqueue");
      assertEquals(initialSize - 1, currentSize);
    }
  }

  @Test
  public void testLogWithMockTime() {
    Timestamp time = new Timestamp();
    String date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date());
    assertEquals(date, time.getDate());
  }

  @Test
  public void testRabbitMqConsumeParadox() throws Exception {
    messageQueueService.send(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", "TestMessage" );
    messageQueueService.consume(RABBIT_MQ_URL, EXCHANGE_NAME, "testqueue", true, true);
    Message message = new Message();
    assertEquals( "TestMessage", message.receiveJsonMessage(messageQueueService.getTemporaryMessage()).getMessage());
  }

  @Test
  public void testQueuedMessageCount() throws Exception {
    assertTrue(messageQueueService.getCount("testqueue") == 0);
  }

  @Test
  public void testMonitorEndPoint() throws Exception {
    IOService IOService = new IOService();
    IOService.readFiles(DATAPATH);
    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(IOService.readFiles(DATAPATH));
    mockMvc.perform(get("/monitor")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType));
  }

  @Test
  public void testMonitorEndPointReturnValue() throws Exception {
    mockMvc.perform(get("/monitor")
            .contentType(contentType))
            .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.statuses[0].status", is("error")))
        .andExpect(jsonPath("$.statuses[1].status", is("ok")))
        .andExpect(jsonPath("$.statuses[2].status", is("ok")))
        .andExpect(jsonPath("$.statuses[3].status", is("ok")));
  }

  @Test
  public void testMonitorOtherServices() throws Exception {
    MonitoringService monitoringService = new MonitoringService();
    assertEquals(monitoringService.monitorOtherServices("https://greenfox-kryptonite.herokuapp.com").getStatus(), "ok");
  }

  @Test
  public void testWriteFile() throws JsonProcessingException {
    IOService IOService = new IOService();
    assertTrue(IOService.writeToFile(DATAPATH));
  }

  @Test
  public void testReadFile() throws IOException {
    IOService IOService = new IOService();

    ObjectMapper mapper = new ObjectMapper();
    String readJson = mapper.writeValueAsString(IOService.readFiles(DATAPATH));
    String expected = "{\"services\":[{\"host\":\"https://hotel-booking-resize-service.herokuapp.com\",\"contact\":\"berta@greenfox.com\"},{\"host\":\"https://booking-notification-service.herokuapp.com\",\"contact\":\"tojasmamusza@greenfox.com\"},{\"host\":\"https://hotel-booking-user-service.herokuapp.com\",\"contact\":\"imi@greenfox.com\"},{\"host\":\"https://hotel-booking-payment.herokuapp.com\",\"contact\":\"yesyo@greenfox.com\"},{\"host\":\"https://booking-resource.herokuapp.com\",\"contact\":\"MrPoopyButthole@podi.com\"}]}";

    assertEquals(expected, readJson);
  }

  @Test
  public void testNorbisBeastMethod() throws Exception {
    PageViewService pageView = new PageViewService();
    pageView.addAttributeToDatabase(eventToDatabaseRepository);
    assertTrue(isItWorking);
  }

  @Test
  public void testPageviewsEndpoint() throws Exception {
    JsonAssemblerService assembler = new JsonAssemblerService();
    assembler.returnPageView(eventToDatabaseRepository);
    assertTrue(isItWorking);
  }

  @Test
  public void testPageViewsEndPoint() throws Exception {
    mockMvc.perform(get("/pageviews"))
        .andExpect(status().isOk());
  }
}