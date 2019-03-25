package com.rps.infrastructure;

public class Action {

  private String playerName;
  private String inviteCode;

  // for deserialisation
  public Action() {
  }

  public Action(String playerName, String inviteCode) {
    this.inviteCode = inviteCode;
    this.playerName = playerName;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getInviteCode() {
    return this.inviteCode;
  }
}
