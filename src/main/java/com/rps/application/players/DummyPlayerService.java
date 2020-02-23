package com.rps.application.players;

import static com.rps.domain.actors.Player.State.PLAYING;
import static com.rps.domain.actors.Player.State.WAITING;

import com.rps.application.RPSException;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.repository.PlayersInMemoryRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("development")
@Service
public class DummyPlayerService implements PlayerService {

    private PlayersInMemoryRepository playersInMemoryRepository;

    @Autowired
    public DummyPlayerService(PlayersInMemoryRepository playersInMemoryRepository) {
        this.playersInMemoryRepository = playersInMemoryRepository;
    }

    public void createPlayer(String name) throws RPSException {
        if (playersInMemoryRepository.findByName(name).isPresent()) {
            throw new RPSException(name + " already exists");
        }
        playersInMemoryRepository.save(new Player(name));
    }

    public Player getPlayer(String name) throws RPSException {
        Optional<Player> playerOptional = playersInMemoryRepository.findByName(name);
        if (playerOptional.isPresent()) {
            return playerOptional.get();
        } else {
            throw new RPSException(name + " not found");
        }
    }

    public Player changePlayerState(String existingPlayerName, Player.State newState)
        throws RPSException {
        Player player = getPlayer(existingPlayerName);
        Player.State currentStateOfPlayer = player.getState();
        if (PLAYING.equals(newState) && WAITING.equals(currentStateOfPlayer)) {
            throw new RPSException("Both players should be READY before starting PLAY");
        }
        player.changeStateTo(newState);
        return player;
    }

    public void deletePlayer(String name) throws RPSException {
        Player player = getPlayer(name);
        if (PLAYING.equals(player.getState())) {
            throw new RPSException("Cannot delete a Player in the middle of a game");
        }
        playersInMemoryRepository.delete(player);
    }
}
