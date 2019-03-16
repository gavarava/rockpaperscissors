package com.rps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_GameplayIT extends RPSTestsMother {

  @Test
  public void shouldGetAnInviteCodeWhenPlayerCreatesInviteForOpponent() throws Exception {

    // Given a registered player
    String playerAName = "PlayerA";
    registerPlayerSuccessfullyUsingAPI(playerAName);

    // When the player asks to create an Invite
    MvcResult createInviteResult = this.mockMvc
        .perform(post("/createInvite/" + playerAName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    assertNotNull(createInviteResult);

    /* The Player must receive an invite code & must be assigned as the first player */
    JSONObject createInviteResultObject = new JSONObject(
        createInviteResult.getResponse().getContentAsString());
    String inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertThat(inviteCode, matchesRegex("^[a-zA-Z0-9]+"));
    JSONObject firstPlayer = (JSONObject) createInviteResultObject.get("firstPlayer");
    assertThat(firstPlayer.get("name"), is("PlayerA"));
  }

  @Test
  public void secondPlayerShouldBeAbleToAcceptInviteFromFirstPlayer() throws Exception {

    // Given a registered player with an Invite Code
    String firstPlayerName = "PlayerXA";
    registerPlayerSuccessfullyUsingAPI(firstPlayerName);

    // And another registered player
    String secondPlayerBName = "PlayerY";
    registerPlayerSuccessfullyUsingAPI(secondPlayerBName);

    // And First Player Creates an Invite
    MvcResult createInviteResult = this.mockMvc
        .perform(post("/createInvite/" + firstPlayerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    assertNotNull(createInviteResult);
    JSONObject createInviteResultObject = new JSONObject(
        createInviteResult.getResponse().getContentAsString());
    String inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertThat(inviteCode, matchesRegex("^[a-zA-Z0-9]+"));
    JSONObject firstPlayer = (JSONObject) createInviteResultObject.get("firstPlayer");
    assertThat(firstPlayer.get("name"), is(firstPlayerName));

    // When the second player accepts the Invite from the First Player
    MvcResult acceptInviteResult = this.mockMvc
        .perform(post("/acceptInvite/" + inviteCode + "/" + secondPlayerBName)
            .contentType(MediaType.APPLICATION_JSON).content("")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    // Then the second player should get all the Game Session Details
    assertNotNull(acceptInviteResult);
    JSONObject acceptInviteResultObject = new JSONObject(
        acceptInviteResult.getResponse().getContentAsString());
    JSONObject firstPlayerObject = (JSONObject) acceptInviteResultObject.get("firstPlayer");
    assertThat(firstPlayerObject.get("name"), is(firstPlayerName));
    JSONObject secondPlayerObject = (JSONObject) acceptInviteResultObject.get("secondPlayer");
    assertThat(secondPlayerObject.get("name"), is(secondPlayerBName));
  }

}
