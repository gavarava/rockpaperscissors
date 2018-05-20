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

    public PlayerCreationDetails createPlayerWithName(String name) {
        Player existingPlayer = playersInMemoryRepository.findByName(name);
        if (existingPlayer != null) {
            return new PlayerCreationDetails(null, "Player with name " + name + " already exists");
        }
        return createNewPlayerWith(name);
    }

    private PlayerCreationDetails createNewPlayerWith(String name) {
        try {
            Player player = new Player(name);
            playersInMemoryRepository.save(player);
            return new PlayerCreationDetails(player, "Player Successfully Created");
        } catch (AlreadyExistsException e) {
            return new PlayerCreationDetails(null, e.getMessage());
        }
    }
}
