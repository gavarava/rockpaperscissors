package com.rps;

import com.rps.domain.PlayersInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RPSApplicationConfiguration {

  @Bean
  public PlayersInMemoryRepository playersInMemoryRepository() {
    return new PlayersInMemoryRepository();
  }

}
