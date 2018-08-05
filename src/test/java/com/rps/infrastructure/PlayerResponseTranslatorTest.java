package com.rps.infrastructure;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import com.rps.application.players.PlayerDetails;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.players.NoPlayerCreatedResponse;
import com.rps.infrastructure.players.PlayerSuccessfullyCreatedResponse;
import com.rps.infrastructure.players.PlayerResponse;
import com.rps.infrastructure.players.PlayerResponseTranslator;
import org.junit.Test;

public class PlayerResponseTranslatorTest {

    @Test
    public void shouldReturnNoPlayerCreatedResponseWhenPlayerAlreadyExists() {
        String playerCreationInfoMessage = "Player with name XYZ already exists";
        PlayerDetails playerDetails = new PlayerDetails(null,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerDetails);
        assertThat(playerResponse, is(instanceOf(NoPlayerCreatedResponse.class)));
        NoPlayerCreatedResponse noPlayerCreatedResponse = (NoPlayerCreatedResponse) playerResponse;
        assertThat(noPlayerCreatedResponse.getMessage(), is(playerCreationInfoMessage));
    }

    @Test
    public void shouldCreatePlayerSuccessfullyCreatedResponseWhenPlayerDoesNotExist() {
        Player player = new Player("NewPlayer");
        String playerCreationInfoMessage = "Player with name was successfully created";
        PlayerDetails playerDetails = new PlayerDetails(player,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerDetails);
        assertThat(playerResponse, is(instanceOf(PlayerSuccessfullyCreatedResponse.class)));
        PlayerSuccessfullyCreatedResponse playerSuccessfullyCreatedResponse = (PlayerSuccessfullyCreatedResponse) playerResponse;
        assertThat(playerSuccessfullyCreatedResponse.getResponseMessage(), is(playerCreationInfoMessage));
    }

}