package com.rps.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.rps.application.players.PlayerDetails;
import com.rps.application.players.PlayerService;
import com.rps.domain.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import org.junit.Before;
import org.junit.Ignore;
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
        PlayerDetails playerDetails = playerService.createPlayerWithName("SomePlayer");
        assertNotNull(playerDetails.getPlayer());
        assertThat(playerDetails.getInfo(), is("Player Successfully Created"));
    }

    @Test
    public void shouldReturnPlayerDetailsWhenPlayerExists() {
        String testPlayerName = "TestPlayerName";
        PlayerDetails createPlayerDetails = playerService.createPlayerWithName(testPlayerName);
        PlayerDetails getPlayerResult = playerService.getPlayer(testPlayerName);
        assertThat(getPlayerResult.getInfo(), is("TestPlayerName is WAITING"));
        Player player = getPlayerResult.getPlayer();
        assertThat(player, is(not(nullValue())));
        assertThat(testPlayerName, is(player.getName()));
    }

    @Test
    public void shouldReturnCorrectMessageWhenPlayerDoesNotExist() {
        PlayerDetails getPlayerResult = playerService.getPlayer("RandomPlayerName");
        assertThat(getPlayerResult.getInfo(), is("The player RandomPlayerName does not exist!!"));
        Player player = getPlayerResult.getPlayer();
        assertThat(player, is(nullValue()));
    }

    @Test
    public void shouldReturnAppropriateResponseWhenPlayerAlreadyExist() {
        playerService.createPlayerWithName("SomePlayer");
        PlayerDetails playerDetails = playerService.createPlayerWithName("SomePlayer");
        assertThat(playerDetails.getInfo(), is("Player with name SomePlayer already exists"));
    }

    @Ignore(value = "TODO")
    public void shouldDeletePlayerWhenRequested() {
        fail();
    }

    @Ignore(value = "TODO")
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenDeletingAPlayerInTheMiddleOfAGame() {
        // Given a player that is in the middle of a game
        // When trying to delete a player
        //It should throw IllegalStateException

    }

}