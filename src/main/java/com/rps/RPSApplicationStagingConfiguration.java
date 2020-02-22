package com.rps;

import com.rps.application.players.PlayerService;
import com.rps.application.players.RealPlayerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("staging")
@Configuration
public class RPSApplicationStagingConfiguration {

    @Bean
    public PlayerService playerService() {
        return new RealPlayerService();
    }
}
