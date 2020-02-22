package com.rps.infrastructure.repository;

import com.rps.domain.actors.Player;
import org.springframework.data.repository.CrudRepository;

/**
 * Using Spring Data CrudRepository
 */
public interface PlayersRepository extends CrudRepository<Player, Long> {
    // https://enterprisecraftsmanship.com/posts/having-the-domain-model-separate-from-the-persistence-model/
}
