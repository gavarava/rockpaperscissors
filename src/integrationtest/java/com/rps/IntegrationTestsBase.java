package com.rps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("inmemory-db")
public class IntegrationTestsBase {

  protected MockMvc mockMvc;
  List<String> playersUsedInTest = new ArrayList<>(2);

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  String getPlayer(String playerName) throws Exception {
    MvcResult getPlayerResult = this.mockMvc
        .perform(get("/player/" + playerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    return getPlayerResult.getResponse().getContentAsString();
  }

  void registerPlayerSuccessfullyUsingAPI(String playerName) throws Exception {
    MvcResult mvcResult = this.mockMvc
        .perform(post("/player/" + playerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated()).andReturn();
    playersUsedInTest.add(playerName);
  }

  void deletePlayer(String playerName) throws Exception {
    MvcResult mvcResult = this.mockMvc
        .perform(delete("/player/" + playerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  String inviteCreatedBy(String firstPlayerName) throws Exception {
    return this.mockMvc
        .perform(post("/createInvite/" + firstPlayerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

  String acceptInvite(String invitee, String inviteCode) throws Exception {
    return this.mockMvc
        .perform(post("/acceptInvite/" + inviteCode + "/" + invitee)
            .contentType(MediaType.APPLICATION_JSON).content("")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

}
