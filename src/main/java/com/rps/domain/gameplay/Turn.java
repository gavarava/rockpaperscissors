package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;
import java.awt.dnd.InvalidDnDOperationException;

public class Turn {

  private Player player;
  private Move move;

  public Turn(Player player, Move move) {
    if (player == null || move == null) {
      throw new InvalidDnDOperationException("A turn cannot be played without a Player & a Move");
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
