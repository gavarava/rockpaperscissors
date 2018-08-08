package com.rps.application.players;

import com.rps.domain.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class PlayerService {

    @Autowired
    PlayersInMemoryRepository playersInMemoryRepository;

    public PlayerService(PlayersInMemoryRepository playersInMemoryRepository) {
        this.playersInMemoryRepository = playersInMemoryRepository;
    }

    public PlayerServiceResponse createPlayerWithName(String name) {
        Player existingPlayer = playersInMemoryRepository.findByName(name);
        if (existingPlayer != null) {
            return new PlayerServiceResponse(null, "Player with name " + name + " already exists");
        }
        return createNewPlayerAs(name);
    }

    private PlayerServiceResponse createNewPlayerAs(String name) {
        try {
            Player player = new Player(name);
            playersInMemoryRepository.save(player);
            return new PlayerServiceResponse(player, "Player Successfully Created");
        } catch (AlreadyExistsException e) {
            return new PlayerServiceResponse(null, e.getMessage());
        }
    }

    public PlayerServiceResponse getPlayer(String name) {
        Player player = playersInMemoryRepository.findByName(name);
        if (player == null) {
            return new PlayerServiceResponse(null, "The player " + name + " does not exist!!");
        }
        return new PlayerServiceResponse(player, "Found player " + player.getName());
    }

    public PlayerServiceResponse changePlayerState(String existingPlayerName, Player.State newState) {
        Player player = playersInMemoryRepository.findByName(existingPlayerName);
        if (player == null) {
            return new PlayerServiceResponse(null, "The player " + existingPlayerName + " does not exist!!");
        }
        Player.State currentStateOfPlayer = player.getState();
        if (Player.State.PLAYING.equals(newState) && Player.State.WAITING.equals(currentStateOfPlayer)) {
            return new PlayerServiceResponse("A Player cannot start playing unless, he/she is READY to play");
        }
        player.changeStateTo(newState);
        return new PlayerServiceResponse(player);
    }

    public PlayerServiceResponse deletePlayer(String name) {
        Player player = playersInMemoryRepository.findByName(name);
        if (player == null) {
            return new PlayerServiceResponse(null, "The player " + name + " does not exist!!");
        }
        if (Player.State.PLAYING.equals(player.getState())) {
            throw new IllegalStateException("Cannot delete a Player in the middle of a game.");
        }
        playersInMemoryRepository.delete(player);
        return new PlayerServiceResponse(MessageFormat.format("Player {0} was deleted successfully", name));
    }
}
