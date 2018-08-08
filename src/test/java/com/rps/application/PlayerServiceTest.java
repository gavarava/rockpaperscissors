package com.rps.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.rps.application.players.PlayerServiceResponse;
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
        PlayerServiceResponse playerServiceResponse = playerService.createPlayerWithName("SomePlayer");
        assertNotNull(playerServiceResponse.getPlayer());
        assertThat(playerServiceResponse.getMessage(), is("Player Successfully Created"));
    }

    @Test
    public void shouldReturnPlayerDetailsWhenPlayerExists() {
        String testPlayerName = "TestPlayerName";
        PlayerServiceResponse createPlayerServiceResponse = playerService.createPlayerWithName(testPlayerName);
        PlayerServiceResponse getPlayerResult = playerService.getPlayer(testPlayerName);
        assertThat(getPlayerResult.getMessage(), is("Found player TestPlayerName"));
        Player player = getPlayerResult.getPlayer();
        assertThat(player, is(not(nullValue())));
        assertThat(testPlayerName, is(player.getName()));
    }

    @Test
    public void shouldReturnCorrectMessageWhenPlayerDoesNotExist() {
        PlayerServiceResponse getPlayerResult = playerService.getPlayer("RandomPlayerName");
        assertThat(getPlayerResult.getMessage(), is("The player RandomPlayerName does not exist!!"));
        Player player = getPlayerResult.getPlayer();
        assertThat(player, is(nullValue()));
    }

    @Test
    public void shouldReturnAppropriateResponseWhenPlayerAlreadyExist() {
        playerService.createPlayerWithName("SomePlayer");
        PlayerServiceResponse playerServiceResponse = playerService.createPlayerWithName("SomePlayer");
        assertThat(playerServiceResponse.getMessage(), is("Player with name SomePlayer already exists"));
    }

    @Test
    public void shouldChangeStateOfPlayerWhenRequestedByTheSamePlayer() {
        String playerName = "PlayerX";
        PlayerServiceResponse playerServiceResponse = playerService.createPlayerWithName(playerName);
        assertThat(playerServiceResponse.getMessage(), is("Player Successfully Created"));
        Player player = playerServiceResponse.getPlayer();
        assertThat(player.getState(), is(Player.State.WAITING));
        assertThat(player, is(not(nullValue())));
        assertThat(playerName, is(player.getName()));

        Player.State newState = Player.State.READY;
        playerService.changePlayerState(playerName, newState);

        PlayerServiceResponse getPlayerServiceResponse = playerService.getPlayer(playerName);
        Player latestPlayerDetails = getPlayerServiceResponse.getPlayer();
        assertThat(latestPlayerDetails.getState(), is(Player.State.READY));
    }

    @Test
    public void shouldReturnErrorMessageDuringChangePlayerStateWhenPlayerDoesNotExist() {
        Player.State newState = Player.State.READY;
        String playerName = "NonExistentPlayer";

        playerService.changePlayerState(playerName, newState);

        PlayerServiceResponse getPlayerServiceResponse = playerService.getPlayer(playerName);
        assertThat(getPlayerServiceResponse.getPlayer(), is(nullValue()));
        assertThat(getPlayerServiceResponse.getMessage(), is("The player NonExistentPlayer does not exist!!"));
    }

    @Test
    public void shouldNotBeAbleToChangeStateFromWaitingToPlayingDirectly() {
        String playerName = "PlayerPlayingAGame";
        createPlayerWithName(playerName);

        PlayerServiceResponse playerServiceResponse = playerService.changePlayerState(playerName, Player.State.PLAYING);
        assertThat(playerServiceResponse.getMessage(), is("A Player cannot start playing unless, he/she is READY to play"));
    }

    @Test
    public void shouldDeletePlayerWhenRequested() {
        String playerNameForTest = "PlayerForDeletion";
        Player player = createPlayerWithName(playerNameForTest);
        playerService.deletePlayer(player.getName());
        PlayerServiceResponse getPlayerResponse = playerService.getPlayer(playerNameForTest);
        assertThat(getPlayerResponse.getPlayer(), is(nullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenTryingToDeletePlayerWhenStateIsPlaying() {
        String playerName = "PlayerPlayingAGame";
        createPlayerWithName(playerName);

        playerService.changePlayerState(playerName, Player.State.READY);
        playerService.changePlayerState(playerName, Player.State.PLAYING);

        playerService.deletePlayer(playerName);
    }

    private Player createPlayerWithName(String playerName) {
        PlayerServiceResponse playerServiceResponse = playerService.createPlayerWithName(playerName);
        return playerServiceResponse.getPlayer();
    }


}