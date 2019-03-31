package com.rps.domain.gameplay;

import static com.rps.domain.gameplay.Move.PAPER;
import static com.rps.domain.gameplay.Move.ROCK;
import static com.rps.domain.gameplay.Move.SCISSORS;
import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoundTest {

  private Round round;

  @Mock
  private Turn firstTurn;

  @Mock
  private Turn secondTurn;

  @Mock
  private Turn thirdTurn;

  @Mock
  private Turn fourthTurn;

  @Mock
  private Player firstPlayer;

  @Mock
  private Player secondPlayer;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void shouldChangeItsStateToPlayingWhenTheFirstTurnIsPlayed() {
    round = new Round(firstTurn);
    assertThat(round.getState(), is(PLAYING));
  }

  @Test
  public void shouldThrowExceptionWhenSamePlayerPlaysConsecutively() {
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(secondTurn.getPlayer()).thenReturn(firstPlayer);
    round = new Round(firstTurn);
    try {
      round.pushLatestTurn(secondTurn);
      fail("Same firstPlayer cannot play consecutively");
    } catch (InvalidOperationException e) {
      assertThat(e.getMessage(),
          is("The same player cannot play again without the opponent making a move"));
    }
  }

  @Test
  public void shouldUpdatePreviousTurnWithTheLastPlayedTurnOnlyWhenTheLatestResultIsNotATie()
      throws InvalidOperationException {
    // Given Player1 & Player2 with their respective turns & a TIE
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(SCISSORS);
    round = new Round(firstTurn);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(SCISSORS);
    round.pushLatestTurn(secondTurn);
    // When the third turn & fourth are played consecutively
    when(thirdTurn.getPlayer()).thenReturn(firstPlayer);
    when(thirdTurn.getMove()).thenReturn(PAPER);
    round.pushLatestTurn(thirdTurn);
    assertNull(round.previousTurn());
    when(fourthTurn.getPlayer()).thenReturn(secondPlayer);
    when(fourthTurn.getMove()).thenReturn(ROCK);
    round.pushLatestTurn(fourthTurn);
    // Then verify that the previous turn is updated with firstTurn
    assertThat(round.previousTurn(), is(thirdTurn));
    assertThat(round.latestTurn(), is(fourthTurn));
  }

  @Test
  public void shouldEvaluateResultWhenLatestRoundIsPushedAndPreviousRoundExists()
      throws InvalidOperationException {
    // Given a Round with a turn played already
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(PAPER);
    round = new Round(firstTurn);
    // When the next turn is played
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(SCISSORS);
    round.pushLatestTurn(secondTurn);
    // Then the Result should be updated in the Round
    Optional<Result> result = round.getResult();
    assertTrue(result.isPresent());
    Result resultValue = result.get();
    assertThat(resultValue.getWinner(), is(secondPlayer));
    assertThat(resultValue.isTie(), is(false));
  }

  @Test
  public void shouldNotEvaluateAnythingWhenItsTheFirstTurnOfTheRound() {
    // Given a player
    // When the first turn is played by the player & no previous turn exists
    Round firstRound = new Round(firstTurn);
    // Then the round should not update Result
    assertFalse(firstRound.getResult().isPresent());
  }

  @Test
  public void shouldAlwaysResetResultToEmptyAfterATie() throws InvalidOperationException {
    // Given a Round with two turns played already
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(PAPER);
    round = new Round(firstTurn);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(PAPER);
    round.pushLatestTurn(secondTurn);
    Optional<Result> result = round.getResult();
    Result resultValue = result.get();
    assertNull(resultValue.getWinner());
    assertThat(resultValue.isTie(), is(true));
    // When firstPlayer takes the Third TURN
    when(thirdTurn.getPlayer()).thenReturn(firstPlayer);
    round.pushLatestTurn(thirdTurn);
    // Then the Result should reset to empty
    Optional<Result> intermediateResult = round.getResult();
    assertThat(intermediateResult, is(Optional.empty()));
  }

  @Test
  public void shouldPlayAPairOfTurnsForEveryWinningOutcome() throws InvalidOperationException {
    // Given a Round with a TIE as the current result
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(PAPER);
    round = new Round(firstTurn);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(PAPER);
    round.pushLatestTurn(secondTurn);
    // When the third & the fourth turn is played consecutively
    when(thirdTurn.getPlayer()).thenReturn(firstPlayer);
    when(thirdTurn.getMove()).thenReturn(SCISSORS);
    round.pushLatestTurn(thirdTurn);
    when(fourthTurn.getPlayer()).thenReturn(secondPlayer);
    when(fourthTurn.getMove()).thenReturn(ROCK);
    round.pushLatestTurn(fourthTurn);
    // Then winning result should be evaluated if there was no tie
    Optional<Result> resultOptional = round.getResult();
    assertThat(resultOptional.isPresent(), is(true));
    assertThat(resultOptional.get().isTie(), is(false));
    assertThat(resultOptional.get().getWinner(), is(secondPlayer));
  }

  @Test
  public void shouldChangeItsStateToOverWhenAPlayerWins() throws InvalidOperationException {
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(PAPER);
    round = new Round(firstTurn);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(SCISSORS);
    round.pushLatestTurn(secondTurn);
    assertThat(round.getState(), is(OVER));
  }
}