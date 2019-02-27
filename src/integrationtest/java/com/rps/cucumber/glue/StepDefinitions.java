package com.rps.cucumber.glue;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;

import java.net.URI;

public class StepDefinitions {

    public static final String APP_URL = "http://localhost:8080";
    private JSONObject acceptInviteResultObject;

    @Given("^two registered players '(.*?)' & '(.*?)'$")
    public void given_two_registered_players(String player1, String player2) throws Exception {
        URI player1Uri = new URI(APP_URL + "/player/" + player1);
        APIClient.doPost(player1Uri, null);
        URI player2Uri = new URI(APP_URL + "/player/" + player2);
        APIClient.doPost(player2Uri, null);
        assert APIClient.performGet(player1Uri).contains(player1);
        assert APIClient.performGet(player2Uri).contains(player2);
    }

    @And("^they are in the same session$")
    public void players_in_same_session() {
    }

    @When("'(.*?)' accepts the invite from '(.*?)' to join the session")
    public void accept_invite(String senderOfInvite, String receiverOfInvite) throws Exception {
    }

    @Then("'(.*?)' & '(.*?)' are a part of the same session")
    public void assert_they_are_in_same_session(String player1, String player2) {
    }

    @When("^'(.*?)' plays '(.*?)' & '(.*?)' plays '(.*?)'$")
    public void play_rock_paper_scissors(String player1, String moveOfPlayer1, String player2, String moveOfPlayer2) {
    }

    @Then("'(.*?)' wins the game")
    public void assert_winner(String expectedWinner) {
    }
}
