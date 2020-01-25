package com.rps.domain;

import com.rps.domain.actors.Player;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Using Spring Data CrudRepository
 */
public class PlayersRepository implements CrudRepository<Player, Long> {

    // https://enterprisecraftsmanship.com/posts/having-the-domain-model-separate-from-the-persistence-model/
    @Override
    public <S extends Player> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Player> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Player> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Player> findAll() {
        return null;
    }

    @Override
    public Iterable<Player> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Player entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
