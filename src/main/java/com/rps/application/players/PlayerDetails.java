package com.rps.application.players;

import com.rps.domain.actors.Player;

public class PlayerDetails {

    private Player player;
    private String info;

    public PlayerDetails(Player player, String info) {
        this.player = player;
        this.info = info;
    }

    public Player getPlayer() {
        return player;
    }

    public String getInfo() {
        return info;
    }
}
