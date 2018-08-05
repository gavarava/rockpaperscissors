package com.rps.infrastructure.players;

import com.rps.application.players.PlayerDetails;

/**
 * This is helping us tailor the response object exactly according to what we get, it would specially be useful when you
 * dont want all of the information in the response, but your service layer actually deals sends you a lot of
 * information.
 */
public class PlayerResponseTranslator {

    private PlayerResponseTranslator() {
    }

    public static PlayerResponse translate(PlayerDetails playerDetails) {
        if (playerDetails.getPlayer() == null) {
            return new NoPlayerCreatedResponse(playerDetails.getInfo());
        }
        return new PlayerSuccessfullyCreatedResponse(playerDetails.getInfo(),
            playerDetails.getPlayer());
    }

}
