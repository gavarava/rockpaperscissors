Feature: Play Rock Paper Scissors

  Scenario: A registered player can invite another registered player to play with
    Given two registered players 'PlayerOne' & 'PlayerTwo'
    When 'PlayerTwo' accepts the invite from 'PlayerOne' to join the session
    Then 'PlayerOne' & 'PlayerTwo' are a part of the same session

  Scenario: Two players in a session can play with each other
    Given two registered players 'PlayerOne' & 'PlayerTwo'
    And they are in the same session
    When 'PlayerOne' plays 'Rock' & 'PlayerTwo' plays 'Paper'
    Then 'PlayerTwo' wins the game