package com.rps;

import com.rps.domain.PlayersInMemoryRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RPSTestsMother {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PlayersInMemoryRepository playersInMemoryRepository;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    String registerPlayerForTest(String playerName) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/register/" + playerName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        return mvcResult.getResponse().getContentAsString();
    }

}
