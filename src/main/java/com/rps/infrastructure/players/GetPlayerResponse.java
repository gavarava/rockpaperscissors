package com.rps.infrastructure.players;

import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;

public class GetPlayerResponse implements PlayerResponse {

    private Player player;

    public GetPlayerResponse(Player player) {
        this.player = player;
    }

    public State getPlayerState() {
        return player.currentState();
    }

    public String getPlayerName() {
        return player.getName();
    }
}
