package com.rps.application.players;

import com.rps.application.RPSException;
import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("staging")
@Service
public class RealPlayerService implements PlayerService {

    @Override
    public void createPlayer(String name) throws RPSException {

    }

    @Override
    public Player getPlayer(String name) throws RPSException {
        return null;
    }

    @Override
    public Player changePlayerState(String existingPlayerName, State newState) throws RPSException {
        return null;
    }

    @Override
    public void deletePlayer(String name) throws RPSException {

    }
}
