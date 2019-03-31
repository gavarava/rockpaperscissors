package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSession {

  private long sessionId;
  private String inviteCode;
  private Player firstPlayer;
  private Player secondPlayer;
  private State state;
  private List<Round> rounds;
  private Player winner;
  private boolean tie;


  public GameSession(Invite invite) {
    this.sessionId = System.currentTimeMillis() + invite.hashCode();
    this.inviteCode = invite.getCode();
    this.firstPlayer = invite.getPlayer();
    this.state = State.WAITING;
    this.rounds = new ArrayList<>(1);
  }

  public State state() {
    return this.state;
  }

  public String getInviteCode() {
    return this.inviteCode;
  }

  public long getSessionId() {
    return this.sessionId;
  }

  public void changeStateTo(State state) {
    this.state = state;
  }

  public Player getFirstPlayer() {
    return this.firstPlayer;
  }

  public Player getSecondPlayer() {
    return this.secondPlayer;
  }

  public State getState() {
    return state;
  }

  public void addOpponent(Player player) {
    this.secondPlayer = player;
  }

  public List<Round> rounds() {
    return Collections.unmodifiableList(rounds);
  }

  public void addRound(Round round) {
    rounds.add(round);
  }

  public Round latestRound() throws InvalidOperationException {
    if (rounds.isEmpty()) {
      throw new InvalidOperationException("No rounds have been started yet");
    }
    return rounds.get(rounds.size() - 1);
  }

  public void setWinner(Player winner) {
    this.winner = winner;
  }

  public Player getWinner() {
    return winner;
  }

  public boolean isTie() {
    return tie;
  }

  public void setTie(boolean tie) {
    this.tie = tie;
  }


  public enum State {
    WAITING, ACCEPTED, PLAYING
  }
}
