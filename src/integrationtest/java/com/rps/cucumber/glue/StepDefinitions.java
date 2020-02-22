package com.rps.cucumber.glue;

import static com.rps.cucumber.glue.APIClient.doGet;
import static com.rps.cucumber.glue.APIClient.doPost;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.rps.RPSApplication;
import com.rps.infrastructure.PlayRequest;
import cucumber.api.java.Before;
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
    private String player1Name;
    private String player2Name;


    @Before
    public void setupScenario() {
        // Start the RPS Application before running the scenario - this means that this test is a System Test ?
        // Every run of the test is starting a new instance of the Application
        // TODO convert System Test to use Docker
        System.setProperty("spring.profiles.active", "development");
        String[] args = {""};
        RPSApplication.main(args);
    }

    @Given("{string} & {string} are registered")
    public void playeronePlayerTwoAreRegistered(String player1, String player2) throws URISyntaxException {
        player1Name = player1;
        player2Name = player2;
        URI player1Uri = new URI(APP_URL + "/player/" + player1);
        doPost(player1Uri, null);
        URI player2Uri = new URI(APP_URL + "/player/" + player2);
        doPost(player2Uri, null);
        assert doGet(player1Uri).contains(player1);
        assert doGet(player2Uri).contains(player2);
    }

    @Given("that {string} and {string} are in the same session")
    public void thatPlayerOneAndPlayerTwoAreInTheSameSession(String player1, String player2) throws URISyntaxException {
      URI createInviteUri = new URI(APP_URL + "/createInvite/" + player1Name);
      String inviteResult = doPost(createInviteUri, null);

      JSONObject createInviteResultObject = new JSONObject(inviteResult);
      inviteCode = (String) createInviteResultObject.get("inviteCode");
      assertNotNull(inviteCode);

      URI acceptInviteUri = new URI(APP_URL + "/acceptInvite/" + inviteCode + "/" + player2Name);
      String acceptInviteResult = doPost(acceptInviteUri, null);
      resultOfAcceptInvite = new JSONObject(acceptInviteResult);

        JSONObject firstPlayer = new JSONObject(resultOfAcceptInvite.get("firstPlayer").toString());
        assertThat(firstPlayer.get("name"), is(player1));

        JSONObject secondPlayer = new JSONObject(resultOfAcceptInvite.get("secondPlayer").toString());
        assertThat(secondPlayer.get("name"), is(player2));
    }

    @When("the players make a move")
    public void thePlayersMakeAMove() throws URISyntaxException {
        URI readplayer1Uri = new URI(APP_URL + "/readyplayer/" + player1Name);
        doPost(readplayer1Uri, null);

        URI readplayer2Uri = new URI(APP_URL + "/readyplayer/" + player2Name);
        doPost(readplayer2Uri, null);

        URI playRequestUri = new URI(APP_URL + "/play");
        doPost(playRequestUri, new PlayRequest(player1Name, inviteCode, "ROCK"));

        URI playRequest2Uri = new URI(APP_URL + "/play");
        doPost(playRequest2Uri, new PlayRequest(player2Name, inviteCode, "PAPER"));
        finalResult = doGet(new URI(APP_URL + "/session/" + inviteCode));
    }

    @Then("both players know the result of the game")
    public void bothPlayersKnowTheResultOfTheGame() {
        JSONObject finalResultObject = new JSONObject(finalResult);
        System.out.println(finalResultObject.toString());
        JSONObject winner = new JSONObject(finalResultObject.get("winner").toString());
        assertThat(winner.get("name"), is(player2Name));
    }
}
