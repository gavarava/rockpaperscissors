package com.rps.domain.actors;


public class Player {

    private long id;
    private String name;
    private State state;
    private int numberOfWins;
    private int numberOfLosses;

    public Player(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name of the Player cannot be null");
        }
        this.name = name;
        this.id = System.currentTimeMillis();
        this.state = State.WAITING;
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

    public State currentState() {
        return this.state;
    }

    public enum State {
        WAITING, PLAYING;
    }
}
