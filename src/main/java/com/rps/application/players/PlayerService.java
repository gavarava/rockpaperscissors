package com.rps.application.players;

import com.rps.application.RPSException;
import com.rps.domain.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static com.rps.domain.actors.Player.State.PLAYING;
import static com.rps.domain.actors.Player.State.WAITING;

@Service
public class PlayerService {

    private static final String PLAYER_DOES_NOT_EXIST = "Player {0} does not exist";

    @Autowired
    PlayersInMemoryRepository playersInMemoryRepository;

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

    public Player changePlayerState(String existingPlayerName, Player.State newState) throws RPSException {
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

    public void deletePlayer(String name) {
        Player player = null;
        try {
            player = playersInMemoryRepository.findByName(name);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (player == null) {
            new PlayerServiceResponse(null, String.format(PLAYER_DOES_NOT_EXIST, name));
            return;
        }
        if (PLAYING.equals(player.getState())) {
            throw new IllegalStateException("Cannot delete a Player in the middle of a game.");
        }
        try {
            playersInMemoryRepository.delete(player);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        new PlayerServiceResponse(MessageFormat.format("Player {0} was deleted successfully", name));
    }
}
