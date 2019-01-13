package com.rps.domain.gameplay;

import com.rps.domain.actors.Player;

public class Invite {
    private Player player;
    private String inviteCode;

    public Invite(Player player) {
        this.player = player;
        this.inviteCode = generateInviteCode();
    }

    private String generateInviteCode() {
        long currentTime = player.hashCode() + System.currentTimeMillis();
        return String.valueOf(currentTime);
    }


    public String getCode() {
        return inviteCode;
    }

    public Player getPlayer() {
        return player;
    }
}
