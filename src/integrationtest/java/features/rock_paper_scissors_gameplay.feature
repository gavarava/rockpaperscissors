Feature: Play Rock Paper Scissors

  Scenario: A registered player can invite another registered player to play with
    Given two registered players 'PlayerOne' & 'PlayerTwo'
    When 'PlayerTwo' accepts the invite from 'PlayerOne' to join the Game Session
    Then 'PlayerOne' & 'PlayerTwo' are a part of the same Game Session

  Scenario: Two players in a Game Session can play with each other
    Given two registered players 'PlayerOne' & 'PlayerTwo'
    And they are in the same Game Session
    When 'PlayerOne' plays 'Rock' & 'PlayerTwo' plays 'Paper'
    Then 'PlayerTwo' wins the game