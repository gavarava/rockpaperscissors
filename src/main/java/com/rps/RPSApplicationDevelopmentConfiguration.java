package com.rps;

import com.rps.application.players.DummyPlayerService;
import com.rps.application.players.PlayerService;
import com.rps.infrastructure.repository.PlayersInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("development")
@Configuration
public class RPSApplicationDevelopmentConfiguration {

    @Bean
    public PlayerService playerService() {
        return new DummyPlayerService(playersInMemoryRepository());
    }

    @Bean
    public PlayersInMemoryRepository playersInMemoryRepository() {
        return new PlayersInMemoryRepository();
    }
}
