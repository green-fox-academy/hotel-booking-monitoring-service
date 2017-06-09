package com.greenfox.kryptonite.projectx.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.greenfox.kryptonite.projectx.repository.HearthbeatRepository;
import java.nio.charset.Charset;

import com.greenfox.kryptonite.projectx.ProjectxApplication;
import com.greenfox.kryptonite.projectx.model.Response;
import com.greenfox.kryptonite.projectx.service.ProjectXService;
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
@SpringBootTest(classes = ProjectxApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class MainRestControllerTest {


  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  private MockMvc mockMvc;
  private HearthbeatRepository hearthbeatRepositoryMock;
  private ProjectXService service;
  @MockBean
  HearthbeatRepository hearthbeatRepository;


  @Autowired
  private WebApplicationContext webApplicationContext;


  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    this.hearthbeatRepositoryMock = Mockito.mock(HearthbeatRepository.class);
    this.service = new ProjectXService();
  }

  @Test
  public void testGetEndpoint() throws Exception {
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status", is("ok")));
  }

  @Test
  public void testResponseWhenNoElementInDatabase() throws Exception {
    Mockito.when(hearthbeatRepositoryMock.count()).thenReturn(0L);
    assertEquals(((Response) service.databaseCheck(hearthbeatRepositoryMock)).getDatabase(), "error");
  }

  @Test
  public void testResponseWhenElementInDatabase() throws Exception {
    Mockito.when(hearthbeatRepositoryMock.count()).thenReturn(3L);
    assertEquals(((Response)service.databaseCheck(hearthbeatRepositoryMock)).getDatabase(), "ok");
  }

  @Test
  public void testGetEndpointWithFilledDatabase() throws Exception {
    BDDMockito.given(hearthbeatRepository.count()).willReturn(1l);
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status", is("ok")))
            .andExpect(jsonPath("$.database", is("ok")));
  }

  @Test
  public void testGetEndpointWithEmptyDatabase() throws Exception {
    BDDMockito.given(hearthbeatRepository.count()).willReturn(0l);
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status", is("ok")))
            .andExpect(jsonPath("$.database", is("error")));
  }
}