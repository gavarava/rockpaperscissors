package com.rps.infrastructure.repository;

import com.rps.domain.actors.Player;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.repository.CrudRepository;

public class PlayersInMemoryRepository implements CrudRepository<Player, Long> {

    private Set<Player> players;

    public PlayersInMemoryRepository() {
        this.players = new HashSet<>();
    }

    @Override
    public Player save(Player player) {
        boolean foundPlayerWithSameName = players.stream()
            .anyMatch(eachPlayerInSet -> eachPlayerInSet.getName().equals(player.getName()));
        if (!foundPlayerWithSameName) {
            players.add(player);
        }
        return player;
    }

    public Player findOne(Long playerId) {
        for (Player player : players) {
            if (playerId == player.getId()) {
                return player;
            }
        }
        return null;
    }

    @Override
    public <S extends Player> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Player> findById(Long id) {
        return Optional.of(players.stream().filter(eachPlayer -> eachPlayer.getId() == id).findFirst())
            .orElse(Optional.empty());
    }


    public Optional<Player> findByName(String name) {
        return Optional.of(players.stream()
            .filter(eachPlayer -> eachPlayer.getName().equals(name))
            .findFirst())
            .orElse(Optional.empty());
    }

    @Override
    public boolean existsById(Long id) {
        return findOne(id) != null;
    }

    @Override
    public Set<Player> findAll() {
        return players;
    }

    @Override
    public Iterable<Player> findAllById(Iterable<Long> playerIds) {
        List<Player> playersFound = new ArrayList<>();
        for (Long playerId : playerIds) {
            boolean contains = players.stream()
                .map(Player::getId)
                .collect(Collectors.toList())
                .contains(playerId);

            if (contains) {
                players.stream()
                    .filter(player -> player.getId() == playerId)
                    .findFirst()
                    .ifPresent(playersFound::add);
            }
        }
        return playersFound;
    }

    @Override
    public long count() {
        return players.size();
    }

    @Override
    public void delete(Player player) {
        if (findOne(player.getId()) != null) {
            players.remove(player);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Player> players) {
        players.forEach(player -> deleteById(player.getId()));
    }

    @Override
    public void deleteAll() {
        players.clear();
    }

    @Override
    public void deleteById(Long id) {
        if (findOne(id) != null) {
            players.remove(findOne(id));
        }
    }
}
