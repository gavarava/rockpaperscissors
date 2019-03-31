package com.rps.infrastructure;

public class PlayRequest {

  private String playerName;
  private String inviteCode;
  private String move;

  // for deserialisation
  public PlayRequest() {
  }

  public PlayRequest(String playerName, String inviteCode, String move) {
    if (playerName == null || inviteCode == null || move == null) {
      throw new IllegalArgumentException(
          "playerName or inviteCode or move must be specified to play your turn");
    }
    this.inviteCode = inviteCode;
    this.playerName = playerName;
    this.move = move;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getInviteCode() {
    return this.inviteCode;
  }

  public String getMove() {
    return move;
  }
}
