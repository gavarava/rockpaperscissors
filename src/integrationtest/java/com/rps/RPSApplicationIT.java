package com.rps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rps.domain.PlayersInMemoryRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration testing the actual API Check the guide here: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("inmemory-db")
public class RPSApplicationIT {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private PlayersInMemoryRepository playersInMemoryRepository;

  private MockMvc mockMvc;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void checkWhetherWebApplicationContextAndTheRepositoryLoadsSuccessfully() {
    assertNotNull(playersInMemoryRepository);
    assertThat(playersInMemoryRepository.count(), Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void shouldReturnActuatorHealthResponse() throws Exception {
    this.mockMvc.perform(get("/actuator/health"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json("{\"status\":\"UP\"}"));
  }

}