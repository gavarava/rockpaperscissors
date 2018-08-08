package com.rps.infrastructure;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import com.rps.application.players.PlayerServiceResponse;
import com.rps.domain.actors.Player;
import com.rps.infrastructure.players.MessageResponse;
import com.rps.infrastructure.players.DetailedPlayerResponse;
import com.rps.infrastructure.players.PlayerResponse;
import com.rps.infrastructure.players.PlayerResponseTranslator;
import org.junit.Test;

public class PlayerResponseTranslatorTest {

    @Test
    public void shouldReturnOnlyMessageResponseWhenPlayerIsNull() {
        String playerCreationInfoMessage = "Player with name XYZ already exists";
        PlayerServiceResponse playerServiceResponse = new PlayerServiceResponse(null,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerServiceResponse);
        assertThat(playerResponse, is(instanceOf(MessageResponse.class)));
        MessageResponse messageResponse = (MessageResponse) playerResponse;
        assertThat(messageResponse.getMessage(), is(playerCreationInfoMessage));
    }

    @Test
    public void shouldReturnDetailedResponseWhenPlayerIsNotNull() {
        Player player = new Player("NewPlayer");
        String playerCreationInfoMessage = "Player with name was successfully created";
        PlayerServiceResponse playerServiceResponse = new PlayerServiceResponse(player,
            playerCreationInfoMessage);
        PlayerResponse playerResponse = PlayerResponseTranslator.translate(playerServiceResponse);
        assertThat(playerResponse, is(instanceOf(DetailedPlayerResponse.class)));
        DetailedPlayerResponse detailedPlayerResponse = (DetailedPlayerResponse) playerResponse;
        assertThat(detailedPlayerResponse.getResponseMessage(), is(playerCreationInfoMessage));
    }

}