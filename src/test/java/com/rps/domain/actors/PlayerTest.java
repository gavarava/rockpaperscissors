package com.rps.domain.actors;

import com.rps.domain.actors.Player.State;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerTest {

    private Player player;

    @Test(expected = IllegalArgumentException.class)
    public void playerCannotBeCreatedWithoutAName() {
        player = new Player(null);
    }

    @Test
    public void shouldHaveStateAsWaitingByDefault() {
        player = new Player("PlayerA");
        assertThat(player.currentState(), is(State.WAITING));
    }

    @Test
    public void shouldBeAbleToChangeOwnState() {
        player = new Player("PlayerA");
        assertThat(player.currentState(), is(State.WAITING));
        player.changeStateTo(State.PLAYING);
        assertThat(player.currentState(), is(State.PLAYING));
    }

}