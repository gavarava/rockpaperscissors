package com.rps;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import static com.rps.domain.actors.Player.State.WAITING;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration testing the actual API Check the guide here: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_PlayerRegistrationIT extends RPSTestsMother {

    @Test
    public void shouldAddPlayerSuccessfullyUsingAPI() throws Exception {
        // Given a playerName
        String playerName = "PlayerOne";

        try {
            // When its is registered
            registerPlayerSuccessfullyUsingAPI(playerName);

            // Then it should not fail
        } catch (Exception e) {
            fail("Registration of Player failed");
        }
    }

    @Test
    public void shouldBeAbleToGetRegisteredPlayerUsingAPI() throws Exception {
        // Given a registered player
        String playerName = "PlayerOne";
        registerPlayerSuccessfullyUsingAPI(playerName);

        //When getPlayer api is invoked
        MvcResult getPlayerResult = this.mockMvc
                .perform(get("/getplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // Then we get the registed player
        JSONObject registeredPlayerObject = new JSONObject(getPlayerResult.getResponse().getContentAsString());
        assertThat(registeredPlayerObject.get("name"), is(playerName));
    }

    @Test
    @Ignore(value = "FIXME - Not working")
    public void shouldNotCreateMultiplePlayersWithSameName() throws Exception {
        // Given a registered player PlayerOne
        String playerName = "PlayerOne";
        registerPlayerSuccessfullyUsingAPI(playerName);

        // When we register another Player with same name
        MvcResult result = this.mockMvc
                .perform(post("/register/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        // Then it should give error message
        assertThat(result.getResponse().getContentAsString(), is(""));
    }

    private void registerPlayerSuccessfullyUsingAPI(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/register/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void thatRegisteringThreePlayersKeepsEveryoneWaiting() throws Exception {
        String player1 = "PlayerA";
        String player2 = "PlayerB";
        String player3 = "PlayerC";

        registerPlayerSuccessfullyUsingAPI(player1);
        registerPlayerSuccessfullyUsingAPI(player2);
        registerPlayerSuccessfullyUsingAPI(player3);

        String player1State = getPlayerState(player1);
        assertThat(player1State, is(WAITING.toString()));
        String player2State = getPlayerState(player2);
        assertThat(player2State, is(WAITING.toString()));
        String player3State = getPlayerState(player3);
        assertThat(player3State, is(WAITING.toString()));


    }

    private String getPlayerState(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/getplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JSONObject playerObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        return playerObject.getString("state");
    }

    @Test
    public void shouldChangePlayerStateToReadyWhenRequestedByPlayer() throws Exception {
        String playerName = "PlayerX";
        registerPlayerSuccessfullyUsingAPI(playerName);

        MvcResult mvcResult = this.mockMvc
                .perform(post("/readyplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response, matchesRegex(
                "^\\{\"id\":[0-9]+,\"name\":\"" +
                        "" + playerName + "\",\"state\":\"READY\",\"numberOfWins\":0,\"numberOfLosses\":0}$"));
    }
}