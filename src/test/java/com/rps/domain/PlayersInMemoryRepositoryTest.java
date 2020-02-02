package com.rps.domain;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.PlayersInMemoryRepository;
import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class PlayersInMemoryRepositoryTest {

  private PlayersInMemoryRepository playersInMemoryRepository;

  @Before
  public void before() {
    playersInMemoryRepository = new PlayersInMemoryRepository();
  }

  @Test
  public void shouldSaveToRepositoryWhenNotExists() throws AlreadyExistsException {
    Player player = new Player("Christer");
    playersInMemoryRepository.save(player);
    assertThat(playersInMemoryRepository.count(), is(greaterThan(0)));
  }

  @Test(expected = AlreadyExistsException.class)
  public void shouldThrowExceptionWhenTryingSaveExistingPlayer() throws AlreadyExistsException {
    Player player = new Player("Christer");
    playersInMemoryRepository.save(player);
    playersInMemoryRepository.save(player);
  }


  @Test
  public void shouldFindOneWhenItExists() throws AlreadyExistsException {
    Player player = new Player("Christer");
    playersInMemoryRepository.save(player);
    assertNotNull(playersInMemoryRepository.findOne(player.getId()));
  }

  @Test
  public void shouldThrowExceptionWwhenPlayerNotFound() {
    assertNull(playersInMemoryRepository.findOne(System.currentTimeMillis()));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowExceptionWhenPlayerNotFoundByName() throws NotFoundException {
    playersInMemoryRepository.findByName("NonExistentPlayer");
  }

  @Test
  public void shouldBeAbleToFindPlayerByName() throws AlreadyExistsException, NotFoundException {
    // Given
    Player player = new Player("PlayerName");
    playersInMemoryRepository.save(player);

    // When
    Player searchedPlayer = playersInMemoryRepository.findByName("PlayerName");

    // Then
    assertThat(searchedPlayer, is(player));
  }

  @Test
  public void findAllShouldReturnAllTheDataSetInTheRepository() throws AlreadyExistsException {
    Player player = new Player("Christer");
    Player player2 = new Player("AnotherPlayer");
    playersInMemoryRepository.save(player);
    playersInMemoryRepository.save(player2);
    Set<Player> players = playersInMemoryRepository.findAll();
    assertThat(players.size(), is(2));
  }

  @Test
  public void shouldAlwaysReturnLatestCount() throws AlreadyExistsException, NotFoundException {
    Player player = new Player("Christer");
    Player player2 = new Player("AnotherPlayer");
    playersInMemoryRepository.save(player);
    playersInMemoryRepository.save(player2);
    playersInMemoryRepository.delete(player);
    assertThat(playersInMemoryRepository.count(), is(1));
  }

  @Test
  public void shouldReturnTrueWhenPlayerExists() throws AlreadyExistsException {
    Player player = new Player("Christer");
    playersInMemoryRepository.save(player);
    assertTrue(playersInMemoryRepository.exists(player.getId()));
  }

  @Test
  public void shouldDeletePlayerByIdWhenExists() throws AlreadyExistsException, NotFoundException {
    Player player = new Player("Christer");
    playersInMemoryRepository.save(player);
    playersInMemoryRepository.deleteById(player.getId());
    assertNull(playersInMemoryRepository.findOne(player.getId()));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowExceptionWhenPlayerotExistsDuringDeleteById() throws NotFoundException {
    playersInMemoryRepository.deleteById(System.currentTimeMillis());
  }

}
