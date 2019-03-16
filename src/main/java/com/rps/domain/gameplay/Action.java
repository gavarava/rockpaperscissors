package com.rps.domain.gameplay;

public class Action {

  private String game;
  private long player;
  private ActionType actionType;

  public Action(String game, long player, ActionType actionType) {
    this.game = game;
    this.player = player;
    this.actionType = actionType;
  }

  public long getPlayer() {
    return player;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public String getGame() {
    return game;
  }

}
