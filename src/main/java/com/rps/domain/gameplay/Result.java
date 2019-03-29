package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;

public class Result {

  private Player winner;
  private boolean tie;


  public boolean isTie() {
    return this.tie;
  }

  public Player getWinner() {
    return this.winner;
  }

  public void setWinner(Player winner) {
    this.winner = winner;
  }

  public void setTie(boolean tie) {
    this.tie = tie;
  }
}
