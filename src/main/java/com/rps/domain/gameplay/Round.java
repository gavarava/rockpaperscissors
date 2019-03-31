package com.rps.domain.gameplay;

import static com.rps.domain.gameplay.Evaluator.*;
import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Round {

  private State state;
  private List<Turn> turns = new ArrayList<>(2);
  private Turn latestTurn;
  private Turn previousTurn;
  private Result result;

  public Round(Turn turn) {
    this.turns.add(turn);
    this.latestTurn = turn;
    this.state = PLAYING;
  }

  public Round.State getState() {
    return state;
  }

  public void pushLatestTurn(Turn turn) throws InvalidOperationException {
    Player currentPlayer = turn.getPlayer();
    if (latestTurn().getPlayer().equals(currentPlayer)) {
      throw new InvalidOperationException(
          "The same player cannot play again without the opponent making a move");
    }
    if (previousResultIsTie()) {
      this.result = null;
      this.previousTurn = null;
      this.latestTurn = turn;
      this.turns.add(turn);
    } else {
      this.previousTurn = turns.get(turns.size() - 1);
      this.latestTurn = turn;
      this.turns.add(turn);
      this.result = evaluate(this.previousTurn, this.latestTurn);
      if (!this.result.isTie()) {
        changeStateTo(OVER);
      }
    }
  }

  private boolean previousResultIsTie() {
    return this.result != null && this.result.isTie();
  }

  private void changeStateTo(State state) throws InvalidOperationException {
    if (OVER.equals(state) && turns.size() < 2) {
      throw new InvalidOperationException("A Round cannot get OVER after playing only one turn");
    }
    this.state = state;
  }

  public Turn latestTurn() {
    return this.latestTurn;
  }

  public Turn previousTurn() {
    return this.previousTurn;
  }

  public Optional<Result> getResult() {
    return Optional.ofNullable(this.result);
  }

  public enum State {
    PLAYING, OVER
  }
}
