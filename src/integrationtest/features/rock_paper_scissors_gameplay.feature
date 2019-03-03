Feature: Play Rock Paper Scissors

  Scenario: A registered player can invite another registered player to play with
    Given a registered player 'PlayerOne'
    When they invite another registered player 'PlayerTwo'
    Then invited player can accept invite to join the game

  Scenario: Two players in a Game Session can play with each other
    Given two registered players 'PlayerOne' & 'PlayerTwo' in a game session
    When 'PlayerOne' plays 'Rock'
    And 'PlayerTwo' plays 'Paper'
    Then 'PlayerTwo' wins the game