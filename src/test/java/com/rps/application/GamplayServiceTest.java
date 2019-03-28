package com.rps.application;

import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.Invite;
import com.rps.domain.gameplay.Round;
import com.rps.domain.gameplay.Turn;
import org.junit.Test;

public class GamplayServiceTest {

  private GameplayService gameplayService;

  @Test
  public void shouldCreateARoundWhenATurnIsPlayedForTheFirstTimeInASession() throws RPSException {
    // Given a GameSession with Two Ready Players & Zero rounds
    GameSession gameSession = createGameSessionForTest();
    // When A turn is played on the GameSession for the first time
    Turn turn = new Turn();
    gameplayService = new GameplayService();
    gameplayService.play(turn, gameSession);
    // Then a new Round with state PLAYING Should be created with the Turn
    assertThat(gameSession.rounds().size(), is(equalTo(1)));
    assertThat(gameSession.rounds().get(0).getState(), is(PLAYING));
  }

  @Test
  public void shouldCreateNewRoundWithStatePlayingWhenPreviousRoundIsOver() throws RPSException {
    // Given a GameSession with Two Ready Players & an existing round which is over
    GameSession gameSession = createGameSessionForTest();
    Round firstRound = new Round(new Turn());
    gameSession.addRound(firstRound);
    assertThat(gameSession.rounds().size(), is(equalTo(1)));
    firstRound.changeStateTo(Round.State.OVER);
    // When a Turn is played on the GameSession
    gameplayService = new GameplayService();
    gameplayService.play(new Turn(), gameSession);
    // Then it should create new round
    assertThat(gameSession.rounds().size(), is(equalTo(2)));
    assertThat(gameSession.rounds().get(1).getState(), is(PLAYING));
  }

  @Test
  public void shouldPushLatestTurnIntoTheLatestRoundWithStatePlaying() throws RPSException {
    // Given a gameSession with two rounds
    GameSession gameSession = createGameSessionForTest();
    Round firstRound = new Round(new Turn());
    firstRound.changeStateTo(OVER);
    gameSession.addRound(firstRound);
    Round secondRound = new Round(new Turn());
    gameSession.addRound(secondRound);
    // When a turn is pushed to the GameSession
    Turn latestTurn = new Turn();
    gameplayService = new GameplayService();
    gameplayService.play(latestTurn, gameSession);
    // Then latestTurn should be pushed inside secondRound
    assertThat(secondRound.latestTurn().hashCode(), is(latestTurn.hashCode()));
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
