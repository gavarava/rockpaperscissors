package com.rps.domain.gameplay;

public enum ActionType {
  TIE(3), ROCK(2), PAPER(1), SCISSORS(0);

  private int value;

  ActionType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case 2:
        return "ROCK";
      case 1:
        return "PAPER";
      case 0:
        return "SCISSORS";
      default:
        return "TIE";
    }
  }
}