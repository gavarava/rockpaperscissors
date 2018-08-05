package com.rps;

import com.rps.domain.PlayersInMemoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration testing the actual API Check the guide here: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_PlayerRegistrationIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PlayersInMemoryRepository playersInMemoryRepository;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldAddPlayerSuccessfullyUsingAPI() throws Exception {
        assertPlayerRegistrationResponse("TestPlayer", registerPlayerForTest("TestPlayer"));
    }

    @Test
    public void shouldNotCreateMultiplePlayersWithSameName() throws Exception {
        assertPlayerRegistrationResponse("Tobias", registerPlayerForTest("Tobias"));
        String response = registerPlayerForTest("Tobias");
        assertThat(response, matchesRegex("^\\{\"message\":\"Player with name Tobias already exists\"}$"));
    }

    @Test
    public void shouldBeAbleToRegisterTwoPlayersConsecutively() throws Exception {
        assertPlayerRegistrationResponse("Aurelius", registerPlayerForTest("Aurelius"));
        assertPlayerRegistrationResponse("Batiatus", registerPlayerForTest("Batiatus"));
    }

    @Test
    public void thatRegisteringThreePlayersKeepsOnePlayerPendingToPlay() throws Exception {
        assertPlayerRegistrationResponse("Caesar", registerPlayerForTest("Caesar"));
        assertPlayerRegistrationResponse("Alexander", registerPlayerForTest("Alexander"));
        assertPlayerRegistrationResponse("Porus", registerPlayerForTest("Porus"));

        // get all three players and check if one of them is still waiting
    }

    @Test
    public void thatTwoRegisteredPlayersCanPlayAgainstEachOther() throws Exception {
        String player1 = "Trish";
        assertPlayerRegistrationResponse(player1, registerPlayerForTest(player1));

        String player2 = "Jacksparrow";
        assertPlayerRegistrationResponse(player2, registerPlayerForTest(player2));

        // player1 ready
        // player2 ready

        //player1 action
        //player2 action

        //try to do something, response should be game over
    }

    @Test
    public void shouldBeAbleToGetRegisteredPlayerDetailsUsingAPI() throws Exception {
            String expectedResult = "^\\{\"responseMessage\":\"Found player Ankit\",\"player\":\\{\"id\":[0-9]+,\"name\":\"Ankit\",\"numberOfWins\":0,\"numberOfLosses\":0}}$";
        String playerName = "Ankit";
        registerPlayerForTest(playerName);

        MvcResult mvcResult = this.mockMvc
                .perform(get("/getplayer/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, matchesRegex(expectedResult));
    }

    private String registerPlayerForTest(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/register/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        return mvcResult.getResponse().getContentAsString();
    }

    private void assertPlayerRegistrationResponse(String playerName, String responseAsString) {
        String expectedResponseRegex = "^\\{\"responseMessage\":\"Player Successfully Created\",\"player\":\\{\"id\":[0-9]+,\"name\":\"" + playerName + "\",\"numberOfWins\":0,\"numberOfLosses\":0}}$";
        assertThat(responseAsString, matchesRegex(expectedResponseRegex));
    }

}