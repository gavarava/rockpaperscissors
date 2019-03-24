package com.rps;

import static com.rps.domain.gameplay.GameSession.State.ACCEPTED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_GameplayIT extends RPSTestsMother {

  @After
  public void tearDown() throws Exception {
    for (String playerName : playersUsedInTest) {
      deletePlayer(playerName);
    }
  }

  @Test
  public void shouldGetAnInviteCodeWhenPlayerCreatesInviteForOpponent() throws Exception {

    // Given a registered player
    String playerAName = "PlayerA";
    registerPlayerSuccessfullyUsingAPI(playerAName);

    // When the player asks to create an Invite
    String createInviteResult = inviteCreatedBy(playerAName);
    assertNotNull(createInviteResult);

    /* The Player must receive an invite code & must be assigned as the first player */
    JSONObject createInviteResultObject = new JSONObject(createInviteResult);
    String inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertThat(inviteCode, matchesRegex("^[a-zA-Z0-9]+"));
    JSONObject firstPlayer = (JSONObject) createInviteResultObject.get("firstPlayer");
    assertThat(firstPlayer.get("name"), is("PlayerA"));
  }

  @Test
  public void secondPlayerShouldBeAbleToAcceptInviteFromFirstPlayer() throws Exception {

    // Given a registered player with an Invite Code
    String firstPlayerName = "PlayerA";
    registerPlayerSuccessfullyUsingAPI(firstPlayerName);

    // And another registered player
    String secondPlayerBName = "PlayerY";
    registerPlayerSuccessfullyUsingAPI(secondPlayerBName);

    // And First Player Creates an Invite
    String createInviteResult = inviteCreatedBy(firstPlayerName);
    assertNotNull(createInviteResult);
    JSONObject createInviteResultObject = new JSONObject(createInviteResult);
    String inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertThat(inviteCode, matchesRegex("^[a-zA-Z0-9]+"));
    JSONObject firstPlayer = (JSONObject) createInviteResultObject.get("firstPlayer");
    assertThat(firstPlayer.get("name"), is(firstPlayerName));

    // When the second player accepts the Invite from the First Player
    String acceptInviteResult = acceptInvite(secondPlayerBName, inviteCode);
    // Then the second player should get all the Game Session Details
    assertNotNull(acceptInviteResult);
    JSONObject acceptInviteResultObject = new JSONObject(acceptInviteResult);
    JSONObject firstPlayerObject = (JSONObject) acceptInviteResultObject.get("firstPlayer");
    assertThat(firstPlayerObject.get("name"), is(firstPlayerName));
    JSONObject secondPlayerObject = (JSONObject) acceptInviteResultObject.get("secondPlayer");
    assertThat(secondPlayerObject.get("name"), is(secondPlayerBName));
  }

  @Ignore(value = "WIP")
  @Test
  public void testPlayRockPaperScissors() throws Exception {
    // Create Player 1
    String playerA = "PlayerA";
    registerPlayerSuccessfullyUsingAPI(playerA);
    // Create Player 2
    String playerB = "PlayerB";
    registerPlayerSuccessfullyUsingAPI(playerB);
    // Player 2 Creates Invite
    String createInviteResult = inviteCreatedBy(playerB);
    JSONObject inviteCodeResult = new JSONObject(createInviteResult);
    String inviteCode = inviteCodeResult.get("inviteCode").toString();
    assertNotNull(inviteCode);
    String sessionId = inviteCodeResult.get("sessionId").toString();
    assertNotNull(sessionId);
    // Check Session using Invite Code
    String currentSessionAsString = getSessionFromInviteCode(inviteCode);
    assertEquals(createInviteResult, currentSessionAsString);
    // Player 1 Accepts Invite
    JSONObject acceptInviteResult = new JSONObject(acceptInvite(playerA, inviteCode));
    // Chceck Session State ACCEPTED
    assertThat(acceptInviteResult.get("state"), is(ACCEPTED.toString()));
    readyPlayer(playerA);
    readyPlayer(playerB);

    // Check Session using Invite Code
    JSONObject sessionWithBothPlayersReady = new JSONObject(getSessionFromInviteCode(inviteCode));
    assertThat(sessionWithBothPlayersReady.get("firstPlayer").toString(), containsString("READY"));
    // playerB is the first player since they created the Invite
    assertThat(sessionWithBothPlayersReady.get("firstPlayer").toString(), containsString(playerB));
    assertThat(sessionWithBothPlayersReady.get("secondPlayer").toString(), containsString("READY"));
    assertThat(sessionWithBothPlayersReady.get("secondPlayer").toString(), containsString(playerA));
    System.out.println(sessionWithBothPlayersReady);

    // Player 1 Plays Rock - State Playing
    play(playerA, "ROCK");
    // Player 2 - Check Session for Gameplay
    JSONObject sessionDuringGameplay = new JSONObject(getSessionFromInviteCode(inviteCode));
    assertThat(sessionDuringGameplay.toString(), containsString("  PLAYING"));
    play(playerB, "SCISSORS");
    // Check Session for Gameplay - Player 1 WINNER
  }

  private String play(String playerA, String action) throws Exception {
    return this.mockMvc
        .perform(post(playerA + "/plays/" + action)
            .contentType(MediaType.APPLICATION_JSON).content("")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

  private String readyPlayer(String player) throws Exception {
    return this.mockMvc
        .perform(post("/readyplayer/" + player)
            .contentType(MediaType.APPLICATION_JSON).content("")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

  private String acceptInvite(String invitee, String inviteCode) throws Exception {
    return this.mockMvc
        .perform(post("/acceptInvite/" + inviteCode + "/" + invitee)
            .contentType(MediaType.APPLICATION_JSON).content("")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

  private String inviteCreatedBy(String firstPlayerName) throws Exception {
    return this.mockMvc
        .perform(post("/createInvite/" + firstPlayerName)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

  private String getSessionFromInviteCode(String inviteCode) throws Exception {
    return this.mockMvc
        .perform(get("/session/" + inviteCode)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

}
