package com.rps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Test;

public class RPSApplication_InvitationIT extends IntegrationTestsBase {

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

}
