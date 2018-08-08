package com.rps.infrastructure.players;

import com.rps.domain.actors.Player;

public class DetailedPlayerResponse implements PlayerResponse {

    private String responseMessage;
    private Player player;

    public DetailedPlayerResponse(String responseMessage, Player player) {
        this.responseMessage = responseMessage;
        this.player = player;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Player getPlayer() {
        return player;
    }
}
