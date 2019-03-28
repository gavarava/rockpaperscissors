package com.rps.application;

import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;

import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Round;
import com.rps.domain.gameplay.Turn;

public class GameplayService {

  public void play(Turn turn, GameSession gameSession) throws RPSException {
    Round latestRound;
    if (gameSession.rounds().size() == 0) {
      createNewRound(turn, gameSession);
    } else if (gameSession.rounds().size() > 0) {
      try {
        latestRound = gameSession.latestRound();
        if (OVER.equals(latestRound.getState())) {
          createNewRound(turn, gameSession);
        } else if (PLAYING.equals(latestRound.getState())) {
          latestRound.pushLatestTurn(turn);
        }
      } catch (InvalidOperationException e) {
        throw new RPSException(e.getMessage());
      }
    }
  }

  private void createNewRound(Turn turn, GameSession gameSession) {
    Round latestRound;
    latestRound = new Round(turn);
    gameSession.addRound(latestRound);
  }

}
