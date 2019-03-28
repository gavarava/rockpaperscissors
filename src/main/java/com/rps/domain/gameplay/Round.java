package com.rps.domain.gameplay;

import static com.rps.domain.gameplay.Round.State.PLAYING;

import java.util.ArrayList;
import java.util.List;

public class Round {

  private State state;
  private List<Turn> turns = new ArrayList<>(2);

  public Round(Turn turn) {
    this.turns.add(turn);
    this.state = PLAYING;
  }

  public Round.State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void changeStateTo(State state) {
    this.state = state;
  }

  public Turn latestTurn() {

    return turns.get(turns.size() - 1);
  }

  public void pushLatestTurn(Turn turn) {
    turns.add(turn);
  }

  public enum State {
    PLAYING, OVER;

    @Override
    public String toString() {
      switch (this) {
        case PLAYING:
          return "PLAYING";
        case OVER:
          return "OVER";
      }
      return "";
    }
  }
}
