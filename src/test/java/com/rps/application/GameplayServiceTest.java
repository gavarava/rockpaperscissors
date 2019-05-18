package com.rps.application;

import static com.rps.domain.gameplay.Move.ROCK;
import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.rps.application.players.PlayerService;
import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.Invite;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import com.rps.infrastructure.PlayRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameplayServiceTest {

  private GameplayService gameplayService;
  private PlayRequest playRequest;

  @Mock
  GameSessionService gameSessionService;

  @Mock
  PlayerService playerService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    gameplayService = new GameplayService(gameSessionService, playerService);
  }

  @Test
  public void shouldCreateARoundWhenATurnIsPlayedForTheFirstTimeInASession()
      throws RPSException, InvalidOperationException {
    // Given a GameSession with Two Ready Players & Zero rounds & a PlayRequest
    GameSession gameSession = createGameSessionForTest();
    Map<String, GameSession> gameSessionMap = new HashMap<>(1);
    gameSessionMap.put(gameSession.getInviteCode(), gameSession);
    when(gameSessionService.sessions()).thenReturn(gameSessionMap);
    playRequest = new PlayRequest(gameSession.getFirstPlayer().getName(),
        gameSession.getInviteCode(),
        "PAPER");
    when(playerService.changePlayerState(gameSession.getFirstPlayer().getName(), State.PLAYING))
        .thenReturn(gameSession.getFirstPlayer());
    // When A turn is played on the GameSession for the first time
    gameplayService.play(playRequest);
    // Then a new Round with state PLAYING Should be created
    assertThat(gameSession.rounds().size(), is(equalTo(1)));
    assertThat(gameSession.rounds().get(0).getState(), is(PLAYING));
  }

  @Test
  public void shouldCreateNewRoundWithStatePlayingWhenPreviousRoundIsOver()
      throws RPSException, InvalidOperationException {
    // Given a GameSession with Two Ready Players & an existing round which is over
    GameSession gameSession = createGameSessionForTest();

    Map<String, GameSession> gameSessionMap = new HashMap<>(1);
    gameSessionMap.put(gameSession.getInviteCode(), gameSession);
    when(gameSessionService.sessions()).thenReturn(gameSessionMap);
    when(playerService.changePlayerState(gameSession.getFirstPlayer().getName(), State.PLAYING))
        .thenReturn(gameSession.getFirstPlayer());
    when(playerService.changePlayerState(gameSession.getSecondPlayer().getName(), State.PLAYING))
        .thenReturn(gameSession.getSecondPlayer());

    PlayRequest playRequest1 = new PlayRequest(gameSession.getFirstPlayer().getName(),
        gameSession.getInviteCode(),
        "PAPER");
    gameplayService.play(playRequest1);
    PlayRequest playRequest2 = new PlayRequest(gameSession.getSecondPlayer().getName(),
        gameSession.getInviteCode(),
        "SCISSORS");
    gameplayService.play(playRequest2);
    assertThat(gameSession.rounds().size(), is(1));
    assertThat(gameSession.rounds().get(0).getState(), is(OVER));
    // When a third Turn is played on the GameSession
    PlayRequest playRequest3 = new PlayRequest(gameSession.getSecondPlayer().getName(),
        gameSession.getInviteCode(),
        "ROCK");
    gameplayService.play(playRequest3);
    // Then it should create new round
    assertThat(gameSession.rounds().size(), is(2));
    assertThat(gameSession.latestRound().getState(), is(PLAYING));
    // And Latest Round should contain the latest turn played
    assertThat(gameSession.latestRound().latestTurn().getPlayer(), is(gameSession.getSecondPlayer()));
    assertThat(gameSession.latestRound().latestTurn().getMove(), is(ROCK));
  }

  private GameSession createGameSessionForTest() {
    Player player1 = new Player("Player1");
    Invite invite = new Invite(player1);
    GameSession gameSession = new GameSession(invite);
    Player player2 = new Player("Player2");
    player1.changeStateTo(State.READY);
    player2.changeStateTo(State.READY);
    gameSession.addOpponent(player2);
    assertThat(gameSession.rounds().size(), is(equalTo(0)));
    return gameSession;
  }
}
