package com.rps.application;

import static com.rps.domain.gameplay.GameSession.State;
import static com.rps.domain.gameplay.GameSession.State.ACCEPTED;
import static com.rps.domain.gameplay.GameSession.State.WAITING;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Invite;
import org.junit.Test;

public class GameSessionServiceTest {

  private GameSessionService gameSessionService;

  @Test
  public void shouldGenerateInviteWithCreatorInformationAndInviteCode() {
    gameSessionService = new GameSessionService();
    Player playerWhoCreatesInvite = new Player("Player1");
    Invite result = gameSessionService.createInvite(playerWhoCreatesInvite);
    assertNotNull("Invite code was not generated", result.getCode());
  }

  @Test
  public void shouldCreateGameSessionWithInvite() {
    gameSessionService = new GameSessionService();
    Player playerWhoCreatesInvite = new Player("Player1");
    Invite invite = gameSessionService.createInvite(playerWhoCreatesInvite);
    assertNotNull("Invite code was not generated", invite.getCode());

    GameSession result = gameSessionService.createSessionFrom(invite);

    assertNotNull(result);
    assertThat(result.state(), is(WAITING));
    assertThat(gameSessionService.sessions().size(), is(greaterThan(0)));
  }

  @Test
  public void shouldReturnGameSessionWhenSearchedWithAnInviteCode() {
    gameSessionService = new GameSessionService();
    Player playerWhoCreatesInvite = new Player("Player1");
    Invite invite = gameSessionService.createInvite(playerWhoCreatesInvite);
    assertNotNull("Invite code was not generated", invite.getCode());

    GameSession createdSession = gameSessionService.createSessionFrom(invite);
    GameSession searchedSession = gameSessionService.findSessionWithInvite(invite.getCode());

    assertNotNull("Game Session not created successfully !!!", searchedSession);
    assertThat(searchedSession, is(createdSession));
  }

  @Test
  public void shouldAddSecondPlayerWhenInviteIsAccepted() throws InvalidOperationException {
    gameSessionService = new GameSessionService();
    Player player1 = new Player("Player1");
    Invite invite = gameSessionService.createInvite(player1);
    assertNotNull("Invite code was not generated", invite.getCode());
    GameSession session = gameSessionService.createSessionFrom(invite);
    assertThat(session.state(), is(State.WAITING));
    long sessionIdForPlayer1 = session.getId();

    Player player2 = new Player("Player2");
    GameSession gameSessionForPlayer2 = gameSessionService.acceptInvite(player2, invite.getCode());

    assertThat(session.state(), is(ACCEPTED));
    assertThat(gameSessionForPlayer2.getId(), is(equalTo(sessionIdForPlayer1)));
    assertThat(session.getFirstPlayer(), is(player1));
    assertThat(session.getSecondPlayer(), is(player2));
  }

  @Test
  public void shouldGiveErrorWhenInvitationIsAcceptedBySamePlayer() {
    gameSessionService = new GameSessionService();
    Player player1 = new Player("Player1");
    Invite invite = gameSessionService.createInvite(player1);
    assertNotNull("Invite code was not generated", invite.getCode());
    GameSession session = gameSessionService.createSessionFrom(invite);
    assertThat(session.state(), is(State.WAITING));

    try {
      gameSessionService.acceptInvite(player1, invite.getCode());
    } catch (InvalidOperationException e) {
      assertThat(e.getMessage(), is("A player cannot accept their own invite"));
    }
  }

}