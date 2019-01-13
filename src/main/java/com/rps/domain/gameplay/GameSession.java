package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;

import static com.rps.domain.gameplay.GameSession.State.ACCEPTED;

public class GameSession {

    private long sessionId;
    private String inviteCode;
    private Player firstPlayer;
    private Player secondPlayer;
    private State state;


    public GameSession(Invite invite) {
        this.sessionId = System.currentTimeMillis() + invite.hashCode();
        this.inviteCode = invite.getCode();
        this.firstPlayer = invite.getPlayer();
        this.state = State.WAITING;
    }

    public State state() {
        return this.state;
    }

    public String getInviteCode() {
        return this.inviteCode;
    }

    public long getId() {
        return this.sessionId;
    }

    public void changeStateTo(State accepted) {
        this.state = ACCEPTED;
    }

    public Player getFirstPlayer() {
        return this.firstPlayer;
    }

    public Player getSecondPlayer() {
        return this.secondPlayer;
    }

    public void addOpponent(Player player) {
        this.secondPlayer = player;
    }

    public enum State
    {
        WAITING, ACCEPTED
    }
}
