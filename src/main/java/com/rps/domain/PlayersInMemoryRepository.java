package com.rps.domain;

import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.CrudRepository;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;

public class PlayersInMemoryRepository implements CrudRepository<Player, Long> {

    private Set<Player> players;

    public PlayersInMemoryRepository() {
        if (players == null) {
            this.players = new HashSet<>();
        }
    }

    @Override
    public void save(Player player) throws AlreadyExistsException {
        boolean addedSucessfully = players.add(player);
        if (!addedSucessfully) {
            throw new AlreadyExistsException("Player with " + player.getName() + " already exists");
        }
    }

    public Player findByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public Player findOne(Long playerId) {
        for (Player player : players) {
            if (playerId == player.getId()) {
                return player;
            }
        }
        return null;
    }

    @Override
    public Set<Player> findAll() {
        return players;
    }

    @Override
    public int count() {
        return players.size();
    }

    @Override
    public void delete(Player player) {
        long playerId = player.getId();
        if (!exists(playerId)) {
            throw new NotFoundException("Player with id " + playerId + " does not exist");
        }
        players.remove(findOne(playerId));
    }

    @Override
    public void deleteById(Long id) {
        if (!exists(id)) {
            throw new NotFoundException("Player with id " + id + " does not exist");
        }
        players.remove(findOne(id));
    }

    @Override
    public boolean exists(Long id) {
        return findOne(id) != null;
    }
}
