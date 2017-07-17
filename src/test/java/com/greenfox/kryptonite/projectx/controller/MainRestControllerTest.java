package com.greenfox.kryptonite.projectx.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.Timestamp;
import com.greenfox.kryptonite.projectx.model.funnels.Funnel;
import com.greenfox.kryptonite.projectx.repository.EventToDatabaseRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelEventRepository;
import com.greenfox.kryptonite.projectx.repository.FunnelRepository;
import com.greenfox.kryptonite.projectx.service.*;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;

import java.nio.charset.Charset;

import com.greenfox.kryptonite.projectx.HotelMonitoringApplication;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
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

  private boolean isItWorking = true;
  private MockMvc mockMvc;
  private HeartbeatRepository heartbeatRepositoryMock;
  private MonitoringService service;
  private HeartbeatRepository nullRepo;
  private static final String DATAPATH = "./src/test/resources/test-monitoring-services.json";

  @MockBean
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private EventToDatabaseRepository eventToDatabaseRepository;

  @Autowired
  private FunnelRepository funnelRepository;

  @Autowired
  private FunnelEventRepository funnelEventRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    this.heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    this.funnelRepository = Mockito.mock(FunnelRepository.class);
    this.service = new MonitoringService();
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
  public void testLogWithMockTime() {
    Timestamp time = new Timestamp();
    String date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date());
    assertEquals(date, time.getDate());
    assertTrue(isItWorking);
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
  public void testPageViewsEndPoint() throws Exception {
    mockMvc.perform(get("/pageviews"))
        .andExpect(status().isOk());
  }

  @Test
  public void testFunnelEndpoint() throws Exception {
    Mockito.when(funnelRepository.save(new Funnel())).thenReturn(new Funnel());
    mockMvc.perform(post("/api/funnels"))
            .andExpect(status().isOk());
  }
}