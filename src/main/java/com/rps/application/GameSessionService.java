package com.rps.application;

import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Invite;

import java.util.HashMap;
import java.util.Map;

public class GameSessionService {

    private final Map<String, GameSession> sessions = new HashMap<>();

    Invite createInvite(Player player) {
        return new Invite(player);
    }

    GameSession createSessionFrom(Invite invite) {
        GameSession gameSession = new GameSession(invite);
        sessions.put(gameSession.getInviteCode(), gameSession);
        return gameSession;
    }

    Map<String, GameSession> sessions() {
        return sessions;
    }

    GameSession findSessionWithInvite(String inviteCode) {
        return sessions.get(inviteCode);
    }

    long acceptInvite(Player player, String inviteCode) throws InvalidOperationException {
        GameSession gameSession = findSessionWithInvite(inviteCode);
        if (player.equals(gameSession.getFirstPlayer())) {
            throw new InvalidOperationException("A player cannot accept their own invite");
        }
        gameSession.addOpponent(player);
        gameSession.changeStateTo(GameSession.State.ACCEPTED);
        return gameSession.getId();
    }
}
