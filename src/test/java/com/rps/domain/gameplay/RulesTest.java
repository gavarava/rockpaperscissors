package com.rps.domain.gameplay;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class RulesTest {
  private Rules rules = new Rules();

  @Test
  public void shouldReturnTieWhenBothMovesAreSame() {
    assertThat(rules.evaluate(ActionType.PAPER, ActionType.PAPER), is(ActionType.TIE));
  }

  @Test
  public void shouldReturnScissorWhenScissorsAndPaper() {
    assertThat(rules.evaluate(ActionType.PAPER, ActionType.SCISSORS), is(ActionType.SCISSORS));
  }

  @Test
  public void shouldReturnRockWhenScissorsAndRock() {
    assertThat(rules.evaluate(ActionType.ROCK, ActionType.SCISSORS), is(ActionType.ROCK));
  }

  @Test
  public void shouldReturnScissorWhenPaperAndRock() {
    assertThat(rules.evaluate(ActionType.ROCK, ActionType.SCISSORS), is(ActionType.ROCK));
  }

}