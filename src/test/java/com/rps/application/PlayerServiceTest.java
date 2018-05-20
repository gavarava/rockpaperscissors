package com.rps.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import com.rps.application.players.PlayerCreationDetails;
import com.rps.application.players.PlayerService;
import com.rps.domain.PlayersInMemoryRepository;
import org.junit.Before;
import org.junit.Test;

public class PlayerServiceTest {

    private PlayerService playerService;

    @Before
    public void beforeTest() {
        PlayersInMemoryRepository playersInMemoryRepository = new PlayersInMemoryRepository();
        playerService = new PlayerService(playersInMemoryRepository);
    }

    @Test
    public void shouldCreatePlayerWhenItDoesNotExist() {
        PlayerCreationDetails playerCreationDetails = playerService.createPlayerWithName("SomePlayer");
        assertNotNull(playerCreationDetails.getPlayer());
        assertThat(playerCreationDetails.getCreationInfo(), is("Player Successfully Created"));
    }

    @Test
    public void shouldReturnAppropriateResponseWhenPlayerAlreadyExist() {
        playerService.createPlayerWithName("SomePlayer");
        PlayerCreationDetails playerCreationDetails = playerService.createPlayerWithName("SomePlayer");
        assertThat(playerCreationDetails.getCreationInfo(), is("Player with name SomePlayer already exists"));
    }


}