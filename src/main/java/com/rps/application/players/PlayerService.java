package com.rps.application.players;

import com.rps.application.RPSException;
import com.rps.domain.actors.Player;

public interface PlayerService {

    void createPlayer(String name) throws RPSException;

    Player getPlayer(String name) throws RPSException;

    Player changePlayerState(String existingPlayerName, Player.State newState)
        throws RPSException;

    void deletePlayer(String name) throws RPSException;
}
