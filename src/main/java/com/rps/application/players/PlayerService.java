package com.rps.application.players;

import com.rps.domain.PlayersInMemoryRepository;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    PlayersInMemoryRepository playersInMemoryRepository;

    public PlayerService(PlayersInMemoryRepository playersInMemoryRepository) {
        this.playersInMemoryRepository = playersInMemoryRepository;
    }

    public PlayerDetails createPlayerWithName(String name) {
        Player existingPlayer = playersInMemoryRepository.findByName(name);
        if (existingPlayer != null) {
            return new PlayerDetails(null, "Player with name " + name + " already exists");
        }
        return createNewPlayerAs(name);
    }

    public PlayerDetails getPlayer(String name) {
        Player player = playersInMemoryRepository.findByName(name);
        if (player == null) {
            return new PlayerDetails(null, "The player " +name+ " does not exist!!");
        }
        return new PlayerDetails(player, "Found player " + player.getName());
    }

    private PlayerDetails createNewPlayerAs(String name) {
        try {
            Player player = new Player(name);
            playersInMemoryRepository.save(player);
            return new PlayerDetails(player, "Player Successfully Created");
        } catch (AlreadyExistsException e) {
            return new PlayerDetails(null, e.getMessage());
        }
    }
}
