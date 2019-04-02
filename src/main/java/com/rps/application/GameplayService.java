package com.rps.application;

import static com.rps.domain.gameplay.Round.State.OVER;
import static com.rps.domain.gameplay.Round.State.PLAYING;

import com.rps.application.players.PlayerService;
import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.Move;
import com.rps.domain.gameplay.Result;
import com.rps.domain.gameplay.Round;
import com.rps.domain.gameplay.Turn;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import com.rps.infrastructure.PlayRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameplayService {

  private PlayerService playerService;

  private GameSessionService gameSessionService;

  public GameplayService(GameSessionService gameSessionService, PlayerService playerService) {
    this.gameSessionService = gameSessionService;
    this.playerService = playerService;
  }

  public void play(PlayRequest playRequest) throws RPSException {
    GameSession currentSession = gameSessionService.sessions().get(playRequest.getInviteCode());
    Player player = playerService.changePlayerState(playRequest.getPlayerName(), State.PLAYING);
    currentSession.changeStateTo(GameSession.State.PLAYING);
    try {
      Turn turn = new Turn(player, Enum.valueOf(Move.class, playRequest.getMove()));
      if (currentSession.rounds().size() == 0) {
        createNewRound(turn, currentSession);
      } else if (currentSession.rounds().size() > 0) {
        Round latestRound = currentSession.latestRound();
        if (OVER.equals(latestRound.getState())) {
          createNewRound(turn, currentSession);
        } else if (PLAYING.equals(latestRound.getState())) {
          latestRound.pushLatestTurn(turn);
        }
      }
      Round latestRoundAfterAllUpdates = currentSession.latestRound();
      Optional<Result> resultOptional = latestRoundAfterAllUpdates.getResult();
      if (resultOptional.isPresent()) {
        currentSession.changeStateTo(GameSession.State.WAITING);
        Result result = resultOptional.get();
        if (result.isTie()) {
          currentSession.setTie(true);
        } else {
          currentSession.setWinner(result.getWinner());
        }
        currentSession.getFirstPlayer().changeStateTo(State.WAITING);
        currentSession.getSecondPlayer().changeStateTo(State.WAITING);
      }
    } catch (InvalidOperationException e) {
      throw new RPSException(e.getMessage());
    }
  }

  private void createNewRound(Turn turn, GameSession gameSession) {
    gameSession.addRound(new Round(turn));
  }

}
