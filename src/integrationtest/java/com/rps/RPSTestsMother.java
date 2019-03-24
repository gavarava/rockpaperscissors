package com.rps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class RPSTestsMother {

  protected MockMvc mockMvc;
  List<String> playersUsedInTest = new ArrayList<>(2);

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  protected void registerPlayerSuccessfullyUsingAPI(String playerName) throws Exception {
    MvcResult mvcResult = this.mockMvc
        .perform(post("/player/" + playerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    playersUsedInTest.add(playerName);
  }

  protected void deletePlayer(String playerName) throws Exception {
    MvcResult mvcResult = this.mockMvc
        .perform(delete("/player/" + playerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
  }

}
