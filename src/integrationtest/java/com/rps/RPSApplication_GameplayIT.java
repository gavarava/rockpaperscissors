package com.rps;

import com.rps.domain.PlayersInMemoryRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RPSApplication_GameplayIT {


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
    @Ignore(value = "To Do")
    public void playARoundOfRockPaperScissorsBetweenTwoPlayers() {

    }
}
