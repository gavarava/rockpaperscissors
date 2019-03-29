package com.rps.domain.gameplay;

import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import java.util.ArrayList;
import java.util.List;

public class Round {

  private State state;
  private List<Turn> turns = new ArrayList<>(2);
  private Turn latestTurn;
  private Turn previousTurn;

  public Round(Turn turn) {
    this.turns.add(turn);
    this.latestTurn = turn;
    this.state = PLAYING;
  }

  public Round.State getState() {
    return state;
  }

  public void changeStateTo(State state) throws InvalidOperationException {
    if (OVER.equals(state) && turns.size() < 2) {
      throw new InvalidOperationException("A Round cannot get OVER after playing only one turn");
    }
    this.state = state;
  }

  public void pushLatestTurn(Turn turn) throws InvalidOperationException {
    Player currentPlayer = turn.getPlayer();
    if (latestTurn().getPlayer().equals(currentPlayer)) {
      throw new InvalidOperationException(
          "The same player cannot play again without the opponent making a move");
    }
    this.previousTurn = turns.get(turns.size() - 1);
    this.latestTurn = turn;
    turns.add(turn);
  }

  public Turn latestTurn() {
    return this.latestTurn;
  }

  public Turn previousTurn() {
    return this.previousTurn;
  }

  public enum State {
    PLAYING, OVER
  }
}
