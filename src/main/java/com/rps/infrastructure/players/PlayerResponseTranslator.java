package com.rps.infrastructure.players;

import com.rps.application.players.PlayerServiceResponse;

/**
 * This is helping us tailor the response object exactly according to what we get, it would specially be useful when you
 * dont want all of the information in the response, but your service layer actually deals sends you a lot of
 * information.
 */
public class PlayerResponseTranslator {

    private PlayerResponseTranslator() {
    }

    public static PlayerResponse translate(PlayerServiceResponse playerServiceResponse) {
        if (playerServiceResponse.getPlayer() == null) {
            return new MessageResponse(playerServiceResponse.getMessage());
        }
        return new DetailedPlayerResponse(playerServiceResponse.getMessage(),
            playerServiceResponse.getPlayer());
    }

}
