package com.rps.domain.gameplay;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TurnTest {

  private Turn turn;

  @Test
  public void shouldThrowExceptionWhenTurnIsNotInitializedWithPlayerAndMove() {
    try {
      turn = new Turn(null, null);
      fail("Turn was initialized without a Player or a Move");
    } catch (Exception e) {
      assertThat(e.getMessage(), is("A turn cannot be played without a Player & a Move"));
    }
  }

}