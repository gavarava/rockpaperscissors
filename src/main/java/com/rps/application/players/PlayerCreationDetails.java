package com.rps.application.players;

import com.rps.domain.actors.Player;

public class PlayerCreationDetails {

    private Player player;
    private String info;

    public PlayerCreationDetails(Player player, String info) {
        this.player = player;
        this.info = info;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCreationInfo() {
        return info;
    }
}
