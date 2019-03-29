package com.rps.domain.gameplay;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import org.junit.Test;

public class GameSessionTest {

  private GameSession gameSession;

  @Test
  public void shouldThrowExceptionWhenAskedForLatestRoundInCaseThereIsNoRound() {
    gameSession = new GameSession(new Invite(new Player("TestPlayer")));
    try {
      gameSession.latestRound();
      fail("Did not throw InvalidOperationException "
          + "when asked for latest round when there is no round");
    } catch (InvalidOperationException e) {
      assertThat(e.getMessage(), is("No rounds have been started yet"));
    }
  }

}