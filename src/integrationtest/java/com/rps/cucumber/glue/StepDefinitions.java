package com.rps.cucumber.glue;

import com.rps.RPSTestsMother;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StepDefinitions extends RPSTestsMother {

    private JSONObject acceptInviteResultObject;

    @Given("^two registered players '(.*?)' & '(.*?)'$")
    public void given_two_registered_players(String player1, String player2) throws Exception {
        registerPlayerSuccessfullyUsingAPI(player1);
        registerPlayerSuccessfullyUsingAPI(player2);
    }

    @And("^they are in the same Game Session$")
    public void players_in_same_session() {
        System.out.println("sesssss");
    }

    @When("'(.*?)' accepts the invite from '(.*?)' to join the Game Session")
    public void accept_invite(String senderOfInvite, String receiverOfInvite) throws Exception {
        // First Player Creates an Invite
        MvcResult createInviteResult = this.mockMvc
                .perform(post("/createInvite/" + senderOfInvite)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertNotNull(createInviteResult);
        JSONObject createInviteResultObject = new JSONObject(createInviteResult.getResponse().getContentAsString());
        String inviteCode = (String) createInviteResultObject.get("inviteCode");
        assertThat(inviteCode, matchesRegex("^[a-zA-Z0-9]+"));
        JSONObject firstPlayer = (JSONObject) createInviteResultObject.get("firstPlayer");
        assertThat(firstPlayer.get("name"), is(senderOfInvite));


        // When the second player accepts the Invite from the First Player
        MvcResult acceptInviteResult = this.mockMvc
                .perform(post("/acceptInvite/" + inviteCode + "/" + receiverOfInvite)
                        .contentType(MediaType.APPLICATION_JSON).content("")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertNotNull(acceptInviteResult);
        acceptInviteResultObject = new JSONObject(acceptInviteResult.getResponse().getContentAsString());
    }

    @Then("'(.*?)' & '(.*?)' are a part of the same Game Session")
    public void assert_they_are_in_same_session(String player1, String player2) {
        // Then the second player should get all the Game Session Details
        JSONObject firstPlayerObject = (JSONObject) acceptInviteResultObject.get("firstPlayer");
        assertThat(firstPlayerObject.get("name"), is(player1));
        JSONObject secondPlayerObject = (JSONObject) acceptInviteResultObject.get("secondPlayer");
        assertThat(secondPlayerObject.get("name"), is(player2));

        // Get Session Details Using API
    }

    @When("^'(.*?)' plays '(.*?)' & '(.*?)' plays '(.*?)'$")
    public void play_rock_paper_scissors(String player1, String moveOfPlayer1, String player2, String moveOfPlayer2) {
        System.out.println("Play");
    }

    @Then("'(.*?)' wins the game")
    public void assert_winner(String expectedWinner) {
        System.out.println("winnerrrrrrrrrrrrr");
    }
}
