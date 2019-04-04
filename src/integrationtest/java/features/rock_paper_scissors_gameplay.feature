Feature: Play Rock Paper Scissors

  Scenario: Two players in a session can play with each other
    Given two registered players 'PlayerOne' & 'PlayerTwo'
    And 'PlayerTwo' accepts the invite from 'PlayerOne'
    And 'PlayerOne' & 'PlayerTwo' are a part of the same session
    And 'PlayerOne' & 'PlayerTwo' are ready
    When 'PlayerOne' plays 'ROCK' & 'PlayerTwo' plays 'PAPER'
    Then 'PlayerTwo' wins the game