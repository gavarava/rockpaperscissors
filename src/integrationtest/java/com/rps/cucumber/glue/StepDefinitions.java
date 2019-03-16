package com.rps.cucumber.glue;

import static com.rps.cucumber.glue.APIClient.doGet;
import static com.rps.cucumber.glue.APIClient.doPost;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.rps.RPSApplication;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.net.URI;
import org.json.JSONObject;

public class StepDefinitions {

  private static final String APP_URL = "http://localhost:8080";
  private JSONObject resultOfAcceptInvite;

  @Before
  public void setupScenario() {
    // Start the RPS Application before running the scenario - this means that this test is a System Test ?
    // Every run of the test is starting a new instance of the Application
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

  @When("'(.*?)' accepts the invite from '(.*?)'")
  public void accept_invite(String invitee, String inviter) throws Exception {
    URI createInviteUri = new URI(APP_URL + "/createInvite/" + inviter);
    String inviteResult = doPost(createInviteUri, null);

    JSONObject createInviteResultObject = new JSONObject(inviteResult);
    String inviteCode = (String) createInviteResultObject.get("inviteCode");
    assertNotNull(inviteCode);

    URI acceptInviteUri = new URI(APP_URL + "/acceptInvite/" + inviteCode + "/" + invitee);
    String acceptInviteResult = doPost(acceptInviteUri, null);
    resultOfAcceptInvite = new JSONObject(acceptInviteResult);
  }

  @Then("'(.*?)' & '(.*?)' are a part of the same session")
  public void assert_they_are_in_same_session(String player1, String player2) {
    System.out.println("-------------------> " + resultOfAcceptInvite);
    JSONObject firstPlayer = new JSONObject(resultOfAcceptInvite.get("firstPlayer").toString());
    assertThat(firstPlayer.get("name"), is(player1));

    JSONObject secondPlayer = new JSONObject(resultOfAcceptInvite.get("secondPlayer").toString());
    assertThat(secondPlayer.get("name"), is(player2));
  }

  @When("^'(.*?)' plays '(.*?)' & '(.*?)' plays '(.*?)'$")
  public void play_rock_paper_scissors(String player1, String moveOfPlayer1, String player2,
      String moveOfPlayer2) {
  }

  @Then("'(.*?)' wins the game")
  public void assert_winner(String expectedWinner) {
  }
}
