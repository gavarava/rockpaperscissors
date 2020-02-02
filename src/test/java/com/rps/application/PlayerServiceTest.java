package com.rps.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import com.rps.application.players.DummyPlayerService;
import com.rps.application.players.PlayerService;
import com.rps.infrastructure.repository.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import org.junit.Before;
import org.junit.Test;

public class PlayerServiceTest {

  private PlayerService playerService;

  @Before
  public void beforeTest() {
    PlayersInMemoryRepository playersInMemoryRepository = new PlayersInMemoryRepository();
    playerService = new DummyPlayerService(playersInMemoryRepository);
  }

  @Test
  public void shouldCreatePlayerWhenItDoesNotExist() {
    try {
      playerService.createPlayer("SomePlayer");
      Player player = playerService.getPlayer("SomePlayer");
      assertThat(player.getName(), is("SomePlayer"));
    } catch (RPSException e) {
      fail("Could not create player: " + e.getMessage());
    }
  }

  @Test
  public void shouldReturnPlayerDetailsWhenPlayerExists() throws RPSException {
    String testPlayerName = "TestPlayerName";
    playerService.createPlayer(testPlayerName);
    Player player = playerService.getPlayer(testPlayerName);
    assertThat(player, is(not(nullValue())));
    assertThat(player.getName(), is(testPlayerName));
  }

  @Test(expected = RPSException.class)
  public void shouldThrowRPSExceptionWhenPlayerDoesNotExist() throws RPSException {
    playerService.getPlayer("ThisPlayerDoesNotExist");
  }

  @Test
  public void shouldChangeStateOfPlayer() throws RPSException {
    // Given PlayerX
    String playerName = "PlayerX";
    playerService.createPlayer(playerName);
    Player player = playerService.getPlayer(playerName);
    assertThat(player.getState(), is(Player.State.WAITING));
    assertThat(player, is(not(nullValue())));
    assertThat(playerName, is(player.getName()));

    Player.State newState = Player.State.READY;
    playerService.changePlayerState(playerName, newState);

    Player updatedPlayerX = playerService.getPlayer(playerName);
    assertThat(updatedPlayerX.getState(), is(Player.State.READY));
  }

  @Test
  public void shouldThrowRPSExceptionWhenPlayerNotFoundDuringChangeOfState() {
    try {
      Player.State newState = Player.State.READY;
      String playerName = "NonExistentPlayer";
      playerService.changePlayerState(playerName, newState);
      Player player = playerService.getPlayer(playerName);
      fail("RPSException not thrown");
    } catch (RPSException e) {
      assertThat(e.getMessage(), is("NonExistentPlayer not found"));
    }
  }

  @Test
  public void shouldNotBeAbleToChangeStateFromWaitingToPlayingDirectly() {
    try {
      String playerName = "PlayerPlayingAGame";
      playerService.createPlayer(playerName);
      playerService.changePlayerState(playerName, Player.State.PLAYING);
    } catch (RPSException e) {
      assertThat(e.getMessage(), is("Both players should be READY before starting PLAY"));
    }
  }

  @Test
  public void shouldDeletePlayerWhenRequested() throws RPSException {
    String playerNameForTest = "PlayerForDeletion";
    playerService.createPlayer(playerNameForTest);
    Player player = playerService.getPlayer(playerNameForTest);

    playerService.deletePlayer(player.getName());
    try {
      Player deletedPlayer = playerService.getPlayer(playerNameForTest);
      fail();
    } catch (RPSException e) {
      assertThat(e.getMessage(), is(playerNameForTest + " not found"));
    }
  }

  @Test
  public void shouldGiveErrorWhenTryingToDeletePlayerInTheMiddleOfAGame() {
    try {
      String playerName = "PlayerPlayingAGame";
      playerService.createPlayer(playerName);

      playerService.changePlayerState(playerName, Player.State.READY);
      playerService.changePlayerState(playerName, Player.State.PLAYING);

      playerService.deletePlayer(playerName);
    } catch (RPSException e) {
      assertThat(e.getMessage(), is("Cannot delete a Player in the middle of a game"));
    }
  }
}