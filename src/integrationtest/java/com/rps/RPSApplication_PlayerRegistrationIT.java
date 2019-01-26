package com.rps;

import com.rps.domain.actors.Player;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration testing the actual API Check the guide here: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_PlayerRegistrationIT extends RPSTestsMother {

    @Test
    public void shouldAddPlayerSuccessfullyUsingAPI() throws Exception {
        assertCreatePlayerResponseString("TestPlayer", registerPlayerForTest("TestPlayer"));
    }

    @Test
    public void shouldNotCreateMultiplePlayersWithSameName() throws Exception {
        assertCreatePlayerResponseString("Tobias", registerPlayerForTest("Tobias"));
        String response = registerPlayerForTest("Tobias");
        assertThat(response, matchesRegex("^\\{\"message\":\"Player with name Tobias already exists\"}$"));
    }

    @Test
    public void shouldBeAbleToRegisterTwoPlayersConsecutively() throws Exception {
        assertCreatePlayerResponseString("Aurelius", registerPlayerForTest("Aurelius"));
        assertCreatePlayerResponseString("Batiatus", registerPlayerForTest("Batiatus"));
    }

    @Test
    @Ignore(value = "ToDo Fix Failure after cleanup")
    public void shouldBeAbleToGetRegisteredPlayerDetailsUsingAPI() throws Exception {
        String playerName = "Ankit";
        registerPlayerForTest(playerName);

        String result = getPlayer(playerName);
        assertGetPlayerResponseString(result, playerName, Player.State.WAITING);
    }


    @Test
    @Ignore(value = "ToDo Fix Failure after cleanup")
    public void thatRegisteringThreePlayersKeepsEveryoneWaiting() throws Exception {
        String player1 = "Caesar";
        assertCreatePlayerResponseString(player1, registerPlayerForTest(player1));
        String player2 = "Alexander";
        assertCreatePlayerResponseString(player2, registerPlayerForTest(player2));
        String player3 = "Porus";
        assertCreatePlayerResponseString(player3, registerPlayerForTest(player3));

        assertGetPlayerResponseString(getPlayer(player1), player1, Player.State.WAITING);
        assertGetPlayerResponseString(getPlayer(player2), player2, Player.State.WAITING);
        assertGetPlayerResponseString(getPlayer(player3), player3, Player.State.WAITING);
    }

    @Test
    public void shouldChangePlayerStateToReadyWhenRequestedByPlayer() throws Exception {
        String playerName = "Pablo";
        assertCreatePlayerResponseString(playerName, registerPlayerForTest(playerName));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/readyplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response =  mvcResult.getResponse().getContentAsString();
        assertThat(response, matchesRegex(
                "^\\{\"responseMessage\":null,\"player\":\\{\"id\":[0-9]+,\"name\":\"" +
                        ""+playerName+"\",\"state\":\"READY\",\"numberOfWins\":0,\"numberOfLosses\":0}}$"));
    }

    private String getPlayer(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/getplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    private void assertCreatePlayerResponseString(String playerName, String responseAsString) {
        String expectedResponseRegex = "^\\{\"responseMessage\":\"Player Successfully Created\",\"player\":\\{\"id\":[0-9]+,\"name\":\"" + playerName + "\",\"state\":\"WAITING\",\"numberOfWins\":0,\"numberOfLosses\":0}}$";
        assertThat(responseAsString, matchesRegex(expectedResponseRegex));
    }

    private void assertGetPlayerResponseString(String actualResult, String expectedPlayersName, Player.State expectedState) {
        String expectedResult = "\"player\":\\{\"id\":[0-9]+,\"name\":\"" + expectedPlayersName + "\",\"state\":\"" + expectedState
                .toString() + "\",\"numberOfWins\":0,\"numberOfLosses\":0}}$";
        assertThat(actualResult, matchesRegex(expectedResult));
    }

}