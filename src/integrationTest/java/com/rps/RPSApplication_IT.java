package com.rps;

import static org.hamcrest.MatchesRegex.matchesRegex;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rps.domain.PlayersInMemoryRepository;
import org.hamcrest.Matchers;
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

/**
 * Integration testing the actual API Check the guide here: https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_IT {

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
    public void checkWhetherWebApplicationContextAndTheRepositoryLoadsSuccessfully() {
        assertThat(playersInMemoryRepository.count(), Matchers.greaterThanOrEqualTo(0));
    }

    @Test
    public void shouldReturnPingResultAsPongAlongWithPlayersReadyAndStateTest() throws Exception {
        this.mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("{\"reply\":\"pong\",\"state\":\"TEST\",\"playersReady\":0}"));
    }

    @Test
    public void shouldAddPlayerSuccessfullyUsingAPI() throws Exception {
        registerPlayerForTest("TestPlayer");
    }

    @Test
    public void shouldBeAbleToRegisterTwoPlayersConsecutively() throws Exception {
        registerPlayerForTest("Jack");
        registerPlayerForTest("Tom");
    }

    @Test
    public void thatRegisteringThreePlayersKeepsOnePlayerPendingToPlay() throws Exception {
        registerPlayerForTest("Jack");
        registerPlayerForTest("Tom");
        registerPlayerForTest("Daniel");

        // get all three players and check if one of them is still waiting
    }

    @Test
    public void thatTwoRegisteredPlayersCanPlayAgainstEachOther() throws Exception {
        String player1 = "Jack";
        registerPlayerForTest(player1);

        String player2 = "Daniel";
        registerPlayerForTest(player2);

        // player1 ready
        // player2 ready

        //player1 action
        //player2 action

        //try to do something, response should be game over
    }

    @Test
    public void shouldBeAbleToGetRegisteredPlayerDetailsUsingAPI() throws Exception {
        String expectedResult = "{ \"playerState\": \"\", \"playerName\": \"Gaurav\" }";
        String playerName = "Ankit";
        registerPlayerForTest(playerName);

        MvcResult mvcResult = this.mockMvc
            .perform(get("/getplayer/"+playerName)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, matchesRegex(expectedResult));
    }

    private void registerPlayerForTest(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
            .perform(post("/register/"+playerName)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        String expectedResponseRegex = "^\\{\"responseMessage\":\"Player Successfully Created\",\"player\":\\{\"id\":[0-9]+,\"name\":\""+playerName+"\",\"numberOfWins\":0,\"numberOfLosses\":0\\}\\}$";
        assertThat(responseAsString, matchesRegex(expectedResponseRegex));
    }

}