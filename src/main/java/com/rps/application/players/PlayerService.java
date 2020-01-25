package com.rps.application.players;

import com.rps.application.RPSException;
import com.rps.domain.actors.Player;

public interface PlayerService {

    public void createPlayer(String name) throws RPSException;

    public Player getPlayer(String name) throws RPSException;

    public Player changePlayerState(String existingPlayerName, Player.State newState)
        throws RPSException;

    public void deletePlayer(String name) throws RPSException;
}
