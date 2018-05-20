package com.rps.domain.actors;

import static com.rps.domain.actors.Player.State.WAITING;

public class Player {

    private long id;
    private String name;
    private State state = WAITING;
    private int numberOfWins;
    private int numberOfLosses;

    public Player(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(int numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }

    public void changeStateTo(State state) {
        this.state = state;
    }

    public enum State {
        WAITING, PLAYING;
    }
}
