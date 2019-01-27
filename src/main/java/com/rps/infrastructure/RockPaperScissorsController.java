package com.rps.infrastructure;

import com.rps.application.GameSessionService;
import com.rps.application.RPSException;
import com.rps.application.players.PlayerService;
import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.ActionType;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RockPaperScissorsController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameSessionService gameSessionService;

    public RockPaperScissorsController(PlayerService playerService, GameSessionService gameSessionService) {
        this.playerService = playerService;
        this.gameSessionService = gameSessionService;
    }

    @GetMapping(value = "/ping", produces = "application/json")
    public DefaultResponse ping() {
        return new DefaultResponse("pong", "TEST");
    }

    @PostMapping(value = "/register/{playerName}", produces = "application/json")
    public ResponseEntity registerPlayer(@PathVariable("playerName") String playerName) {
        try {
            playerService.createPlayer(playerName);
            return ResponseEntity.ok().build();
        } catch (RPSException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/createInvite/{playerName}", produces = "application/json")
    public ResponseEntity<GameSession> createInvite(@PathVariable("playerName") String playerName) {
        Player player = null;
        try {
            player = playerService.getPlayer(playerName);
        } catch (RPSException e) {
            e.printStackTrace();
        }
        GameSession session = gameSessionService.createSessionFrom(new Invite(player));
        return ResponseEntity.ok(session);
    }

    @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}", produces = "application/json")
    public GameSession acceptInvite(@PathVariable("inviteCode") String inviteCode, @PathVariable("playerName") String playerName) throws InvalidOperationException {
        Player player = null;
        try {
            player = playerService.getPlayer(playerName);
        } catch (RPSException e) {
            e.printStackTrace();
        }
        return gameSessionService.acceptInvite(player, inviteCode);
    }

    @GetMapping(value = "/getplayer/{playername}", produces = "application/json")
    public ResponseEntity getPlayer(@PathVariable("playername") String playerName) {
        try {
            Player player = playerService.getPlayer(playerName);
            return ResponseEntity.ok(player);
        } catch (RPSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/readyplayer/{playername}", produces = "application/json")
    public ResponseEntity ready(@PathVariable("playername") String playerName) {
        try {
            Player player = playerService.changePlayerState(playerName, Player.State.READY);
            return ResponseEntity.ok(player);
        } catch (RPSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/actionType/{playerid}/{actionType}", produces = "application/json")
    public ResponseEntity.BodyBuilder play(@PathVariable("playerid") long playerId, @PathVariable("actionType") ActionType actionType) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED);
    }

}