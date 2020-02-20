Feature: Players can register and invite each other to play the game of Rock-Paper-Scissors

  Background: A running instance of the Rock-Paper-Scissors service
    Given 'PlayerOne' & 'PlayerTwo' are registered

  Scenario: Two players in the same session can play against each other
    Given that 'PlayerOne' and 'PlayerTwo' are in the same session
    When the players make a move
    Then both players know the result of the game