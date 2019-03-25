package com.rps.application.players;

import static com.rps.domain.actors.Player.State.PLAYING;
import static com.rps.domain.actors.Player.State.WAITING;

import com.rps.application.RPSException;
import com.rps.domain.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

  @Autowired
  private PlayersInMemoryRepository playersInMemoryRepository;

  public PlayerService(PlayersInMemoryRepository playersInMemoryRepository) {
    this.playersInMemoryRepository = playersInMemoryRepository;
  }

  public void createPlayer(String name) throws RPSException {
    try {
      playersInMemoryRepository.save(new Player(name));
    } catch (AlreadyExistsException e) {
      throw new RPSException(e.getMessage());
    }
  }

  public Player getPlayer(String name) throws RPSException {
    try {
      return playersInMemoryRepository.findByName(name);
    } catch (NotFoundException e) {
      throw new RPSException(e.getMessage());
    }
  }

  public Player changePlayerState(String existingPlayerName, Player.State newState)
      throws RPSException {
    try {
      Player player = playersInMemoryRepository.findByName(existingPlayerName);
      Player.State currentStateOfPlayer = player.getState();
      if (PLAYING.equals(newState) && WAITING.equals(currentStateOfPlayer)) {
        throw new RPSException("Both players should be READY before starting PLAY");
      }
      player.changeStateTo(newState);
      return player;
    } catch (NotFoundException e) {
      throw new RPSException(e.getMessage());
    }
  }

  public void deletePlayer(String name) throws RPSException {
    try {
      Player player = playersInMemoryRepository.findByName(name);
      if (PLAYING.equals(player.getState())) {
        throw new RPSException("Cannot delete a Player in the middle of a game");
      }
      playersInMemoryRepository.delete(player);
    } catch (NotFoundException e) {
      throw new RPSException(e.getMessage());
    }
  }
}
