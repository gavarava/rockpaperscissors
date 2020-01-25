package com.rps;

import static com.rps.domain.gameplay.GameSession.State.ACCEPTED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rps.infrastructure.PlayRequest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RPSApplication_GameplayIT extends IntegrationTestsBase {

  @After
  public void tearDown() throws Exception {
    for (String playerName : playersUsedInTest) {
      deletePlayer(playerName);
    }
  }

  @Test
  public void shouldReturnBadRequestWhenPlayCalledWithIncorrectData() throws Exception {
    // Create Player 1
    String playerA = "PlayerFF";
    registerPlayerSuccessfullyUsingAPI(playerA);
    // Create Player 2
    String playerB = "PlayerOO";
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

    // PlayerA Plays Rock
    PlayRequest playRequest = new PlayRequest("InvalidName", inviteCode, "ROCK");
    String result = this.mockMvc
        .perform(post("/play")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"playerName\":\"" + playRequest.getPlayerName() + "\",\"inviteCode\":\""
                + playRequest
                .getInviteCode() + "\",\"move\":\"" + playRequest.getMove() + "\"}")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
    assertThat(result, is("InvalidName not found"));
  }

  @Test
  public void testPlayRockPaperScissors() throws Exception {
    // Create Player 1
    String playerA = "PlayerK";
    registerPlayerSuccessfullyUsingAPI(playerA);
    // Create Player 2
    String playerB = "PlayerL";
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

    // PlayerA Plays Rock
    play(new PlayRequest(playerA, inviteCode, "ROCK"));
    assertThat(playerA + " state was not changed to PLAYING",
        getPlayer(playerA), containsString("\"state\":\"PLAYING\""));
    // Player 2 - Check Session for Gameplay
    JSONObject sessionDuringGameplay = new JSONObject(getSessionFromInviteCode(inviteCode));
    assertThat(sessionDuringGameplay.toString(), containsString("PLAYING"));
    // PlayerB plays Scissors
    play(new PlayRequest(playerB, inviteCode, "SCISSORS"));
    assertThat(playerB + " state was not changed to WAITING",
        getPlayer(playerB), containsString("\"state\":\"WAITING\""));
    // Check Session for Gameplay - PlayerA is the winner
    JSONObject sessionAfterPlayerBHasMadeAMove = new JSONObject(
        getSessionFromInviteCode(inviteCode));
    assertThat(sessionAfterPlayerBHasMadeAMove.toString(),
        containsString("\"tie\":false,\"winner\":{\"name\":\"PlayerK\""));
    // Both the players will be WAITING after the winner is declared
    assertThat(getPlayer(playerA), containsString("\"state\":\"WAITING\""));
    assertThat(getPlayer(playerB), containsString("\"state\":\"WAITING\""));
    System.out.println(new JSONObject(getSessionFromInviteCode(inviteCode)));
  }

  private String play(PlayRequest playRequest) throws Exception {
    return this.mockMvc
        .perform(post("/play")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"playerName\":\"" + playRequest.getPlayerName() + "\",\"inviteCode\":\""
                + playRequest
                .getInviteCode() + "\",\"move\":\"" + playRequest.getMove() + "\"}")
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

  private String getSessionFromInviteCode(String inviteCode) throws Exception {
    return this.mockMvc
        .perform(get("/session/" + inviteCode)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }
}