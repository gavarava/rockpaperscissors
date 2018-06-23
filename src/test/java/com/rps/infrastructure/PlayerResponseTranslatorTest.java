package com.rps.infrastructure;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import com.rps.application.players.PlayerCreationDetails;
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
        PlayerCreationDetails playerCreationDetails = new PlayerCreationDetails(null,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerCreationDetails);
        assertThat(playerResponse, is(instanceOf(NoPlayerCreatedResponse.class)));
        NoPlayerCreatedResponse noPlayerCreatedResponse = (NoPlayerCreatedResponse) playerResponse;
        assertThat(noPlayerCreatedResponse.getMessage(), is(playerCreationInfoMessage));
    }

    @Test
    public void shouldCreatePlayerSuccessfullyCreatedResponseWhenPlayerDoesNotExist() {
        Player player = new Player("NewPlayer");
        String playerCreationInfoMessage = "Player with name was successfully created";
        PlayerCreationDetails playerCreationDetails = new PlayerCreationDetails(player,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerCreationDetails);
        assertThat(playerResponse, is(instanceOf(PlayerSuccessfullyCreatedResponse.class)));
        PlayerSuccessfullyCreatedResponse playerSuccessfullyCreatedResponse = (PlayerSuccessfullyCreatedResponse) playerResponse;
        assertThat(playerSuccessfullyCreatedResponse.getResponseMessage(), is(playerCreationInfoMessage));
    }

}