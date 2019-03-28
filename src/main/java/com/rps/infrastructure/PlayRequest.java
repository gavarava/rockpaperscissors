package com.rps.infrastructure;

public class PlayRequest {

  private String playerName;
  private String inviteCode;

  // for deserialisation
  public PlayRequest() {
  }

  public PlayRequest(String playerName, String inviteCode) {
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
