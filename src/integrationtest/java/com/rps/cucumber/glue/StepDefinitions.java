package com.rps.cucumber.glue;

import static com.rps.cucumber.glue.APIClient.doGet;
import static com.rps.cucumber.glue.APIClient.doPost;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.rps.RPSApplication;
import com.rps.infrastructure.PlayRequest;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;

public class StepDefinitions {

  private static final String APP_URL = "http://localhost:8000";
  private JSONObject resultOfAcceptInvite;
  private String inviteCode;
  private String finalResult;

  @Before
  public void setupScenario() {
    // Start the RPS Application before running the scenario - this means that this test is a System Test ?
    // Every run of the test is starting a new instance of the Application
    // TODO convert System Test to use Docker
    String[] args = {""};
    RPSApplication.main(args);
  }

  @Given("^two registered players '(.*?)' & '(.*?)'$")
  public void given_two_registered_players(String player1, String player2) throws Exception {
    URI player1Uri = new URI(APP_URL + "/player/" + player1);
    doPost(player1Uri, null);
    URI player2Uri = new URI(APP_URL + "/player/" + player2);
    doPost(player2Uri, null);
    assert doGet(player1Uri).contains(player1);
    assert doGet(player2Uri).contains(player2);
  }

  @And("'(.*?)' accepts the invite from '(.*?)'")
  public void accept_invite(String invitee, String inviter) throws Exception {
    URI createInviteUri = new URI(APP_URL + "/createInvite/" + inviter);
    String inviteResult = doPost(createInviteUri, null);

    JSONObject createInviteResultObject = new JSONObject(inviteResult);
    inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertNotNull(inviteCode);

    URI acceptInviteUri = new URI(APP_URL + "/acceptInvite/" + inviteCode + "/" + invitee);
    String acceptInviteResult = doPost(acceptInviteUri, null);
    resultOfAcceptInvite = new JSONObject(acceptInviteResult);
  }

  @And("'(.*?)' & '(.*?)' are a part of the same session")
  public void assert_they_are_in_same_session(String player1, String player2) {
    JSONObject firstPlayer = new JSONObject(resultOfAcceptInvite.get("firstPlayer").toString());
    assertThat(firstPlayer.get("name"), is(player1));

    JSONObject secondPlayer = new JSONObject(resultOfAcceptInvite.get("secondPlayer").toString());
    assertThat(secondPlayer.get("name"), is(player2));
  }

  @And("'(.*?)' & '(.*?)' are ready")
  public void ready_players(String player1, String player2) throws URISyntaxException {
    URI readplayer1Uri = new URI(APP_URL + "/readyplayer/" + player1);
    doPost(readplayer1Uri, null);

    URI readplayer2Uri = new URI(APP_URL + "/readyplayer/" + player2);
    doPost(readplayer2Uri, null);
  }

  @When("^'(.*?)' plays '(.*?)' & '(.*?)' plays '(.*?)'$")
  public void play_rock_paper_scissors(String player1, String moveOfPlayer1, String player2,
      String moveOfPlayer2) throws URISyntaxException {

    URI playRequestUri = new URI(APP_URL + "/play");
    String result = doPost(playRequestUri, new PlayRequest(player1, inviteCode, moveOfPlayer1));

    URI playRequest2Uri = new URI(APP_URL + "/play");
    doPost(playRequest2Uri, new PlayRequest(player2, inviteCode, moveOfPlayer2));
    finalResult = doGet(new URI(APP_URL + "/session/" + inviteCode));
  }

  @Then("'(.*?)' wins the game")
  public void assert_winner(String expectedWinner) {
    JSONObject finalResultObject = new JSONObject(finalResult);
    JSONObject winner = new JSONObject(finalResultObject.get("winner").toString());
    assertThat(winner.get("name"), is(expectedWinner));
  }
}
