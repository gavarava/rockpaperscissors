package com.rps.domain;

import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.CrudRepository;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Reinventing the wheel with own CrudRepository
 */
public class PlayersInMemoryRepository implements CrudRepository<Player, Long> {

  private Set<Player> players;

  public PlayersInMemoryRepository() {
    this.players = new HashSet<>();
  }

  @Override
  public void save(Player player) throws AlreadyExistsException {
    boolean foundPlayerWithSameName = players.stream()
        .anyMatch(eachPlayerInSet -> eachPlayerInSet.getName().equals(player.getName()));
    if (foundPlayerWithSameName) {
      throw new AlreadyExistsException(player.getName() + " already exists");
    } else {
      players.add(player);
    }
  }

  public Player findByName(String name) throws NotFoundException {
    return players.stream().filter(eachPlayer -> eachPlayer.getName().equals(name)).findFirst()
        .orElseThrow(() -> new NotFoundException(name + " not found"));
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
  public void delete(Player player) throws NotFoundException {
    long playerId = player.getId();
    if (!exists(playerId)) {
      throw new NotFoundException("Player with id " + playerId + " does not exist");
    }
    players.remove(findOne(playerId));
  }

  @Override
  public void deleteById(Long id) throws NotFoundException {
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
