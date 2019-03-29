package com.rps.domain.gameplay;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.Round.State;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
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
  private Player firstPlayer;

  @Mock
  private Player secondPlayer;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void shouldNotChangeStateToOverWhenOnlyOneTurnPlayedInARound() {
    round = new Round(firstTurn);
    try {
      round.changeStateTo(State.OVER);
      fail(
          "Expected an exception when trying to change state of a Round to over with only one turn");
    } catch (InvalidOperationException e) {
      assertThat(e.getMessage(), is("A Round cannot get OVER after playing only one turn"));
    }
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
  public void shouldAlwaysUpdatePreviousTurnWhenTheLatestTurnIsUpdated()
      throws InvalidOperationException {
    // Given Player1 & Player2 with their respective turns in the first round
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    round = new Round(firstTurn);
    // When the second turn is player by Player2
    round.pushLatestTurn(secondTurn);
    // Then verify that the previous turn is updated with firstTurn
    assertThat(round.previousTurn(), is(firstTurn));
    assertThat(round.latestTurn(), is(secondTurn));
  }
}