package com.rps.application.players;

import com.rps.domain.actors.Player;

public class PlayerServiceResponse {

    private Player player;
    private String message;

    public PlayerServiceResponse(Player player) {
        this.player = player;
    }

    public PlayerServiceResponse(String message) {
        this.message = message;
    }

    public PlayerServiceResponse(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }
}
