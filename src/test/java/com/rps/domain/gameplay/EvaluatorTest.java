package com.rps.domain.gameplay;

import static com.rps.domain.gameplay.Move.PAPER;
import static com.rps.domain.gameplay.Move.ROCK;
import static com.rps.domain.gameplay.Move.SCISSORS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.rps.domain.actors.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EvaluatorTest {

  private Evaluator evaluator;

  @Mock
  private Player firstPlayer;

  @Mock
  private Player secondPlayer;

  @Mock
  private Turn firstTurn;

  @Mock
  private Turn secondTurn;

  @Before
  public void setup() {
    initMocks(this);
    evaluator = new Evaluator();
  }

  @Test
  public void evaluatePaperCoversRock() {
    when(firstTurn.getMove()).thenReturn(PAPER);
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(secondTurn.getMove()).thenReturn(ROCK);
    Result result = evaluator.evaluate(firstTurn, secondTurn);
    assertThat(result.isTie(), is(false));
    assertThat(result.getWinner(), is(firstPlayer));
  }

  @Test
  public void evaluateScissorCutsPaper() {
    // when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(PAPER);
    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(SCISSORS);
    Result result = evaluator.evaluate(firstTurn, secondTurn);
    assertThat(result.isTie(), is(false));
    assertThat(result.getWinner(), is(secondPlayer));
  }

  @Test
  public void evaluateRockBreaksScissors() {
    when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(firstTurn.getMove()).thenReturn(ROCK);
//    when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    when(secondTurn.getMove()).thenReturn(SCISSORS);
    Result result = evaluator.evaluate(firstTurn, secondTurn);
    assertThat(result.isTie(), is(false));
    assertThat(result.getWinner(), is(firstPlayer));
  }

  @Test
  public void evaluateSameMoveGivesATie() {
    when(firstTurn.getMove()).thenReturn(PAPER);
    // when(firstTurn.getPlayer()).thenReturn(firstPlayer);
    when(secondTurn.getMove()).thenReturn(PAPER);
    // when(secondTurn.getPlayer()).thenReturn(secondPlayer);
    Result result = evaluator.evaluate(firstTurn, secondTurn);
    assertThat(result.isTie(), is(true));
  }
}
