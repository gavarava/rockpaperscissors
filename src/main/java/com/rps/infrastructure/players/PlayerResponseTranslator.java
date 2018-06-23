package com.rps.infrastructure.players;

import com.rps.application.players.PlayerCreationDetails;

/**
 * This is helping us tailor the response object exactly according to what we get, it would specially be useful when you
 * dont want all of the information in the response, but your service layer actually deals sends you a lot of
 * information.
 */
public class PlayerResponseTranslator {

    private PlayerResponseTranslator() {
    }

    public static PlayerResponse translate(PlayerCreationDetails playerCreationDetails) {
        if (playerCreationDetails.getPlayer() == null) {
            return new NoPlayerCreatedResponse(playerCreationDetails.getCreationInfo());
        }
        return new PlayerSuccessfullyCreatedResponse(playerCreationDetails.getCreationInfo(),
            playerCreationDetails.getPlayer());
    }

}
