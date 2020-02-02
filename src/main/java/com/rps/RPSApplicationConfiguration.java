package com.rps;

import com.rps.infrastructure.repository.PlayersInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RPSApplicationConfiguration {

  @Bean
  public PlayersInMemoryRepository playersInMemoryRepository() {
    return new PlayersInMemoryRepository();
  }

}
