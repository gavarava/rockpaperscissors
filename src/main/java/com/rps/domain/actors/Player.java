package com.rps.domain.actors;


public class Player {

  private long id;
  private String name;
  private State state;

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

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void changeStateTo(State state) {
    this.state = state;
  }

  State currentState() {
    return this.state;
  }

  public enum State {
    WAITING, PLAYING, READY;

    @Override
    public String toString() {
      switch (this) {
        case READY:
          return "READY";
        case PLAYING:
          return "PLAYING";
        case WAITING:
          return "WAITING";
      }
      return "";
    }
  }
}
