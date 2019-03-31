package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;

public class Turn {

  private Player player;
  private Move move;

  public Turn(Player player, Move move) throws InvalidOperationException {
    if (player == null || move == null) {
      throw new InvalidOperationException("A turn cannot be played without a Player & a Move");
    }
    this.player = player;
    this.move = move;
  }

  public Player getPlayer() {
    return this.player;
  }

  public Move getMove() {
    return this.move;
  }
}
