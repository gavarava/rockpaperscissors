package com.rps.infrastructure;

import com.rps.application.GameSessionService;
import com.rps.application.players.PlayerService;
import com.rps.application.players.PlayerServiceResponse;
import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.ActionType;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Invite;
import com.rps.infrastructure.players.PlayerResponse;
import com.rps.infrastructure.players.PlayerResponseTranslator;
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
    public ResponseEntity<PlayerResponse> registerPlayer(@PathVariable("playerName") String playerName) {
        PlayerServiceResponse playerServiceResponse = playerService.createPlayerWithName(playerName);
        return ResponseEntity.ok(PlayerResponseTranslator.translate(playerServiceResponse));
    }

    @PostMapping(value = "/createInvite/{playerName}", produces = "application/json")
    public GameSession createInvite(@PathVariable("playerName") String playerName) {
        PlayerServiceResponse response = playerService.getPlayer(playerName);
        return gameSessionService.createSessionFrom(new Invite(response.getPlayer()));
    }

    @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}", produces = "application/json")
    public GameSession acceptInvite(@PathVariable("inviteCode") String inviteCode, @PathVariable("playerName") String playerName) throws InvalidOperationException {
        PlayerServiceResponse response = playerService.getPlayer(playerName);
        return gameSessionService.acceptInvite(response.getPlayer(), inviteCode);
    }

    @GetMapping(value = "/getplayer/{playername}", produces = "application/json")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable("playername") String playerName) {
        PlayerServiceResponse playerServiceResponse = playerService.getPlayer(playerName);
        return ResponseEntity.ok(PlayerResponseTranslator.translate(playerServiceResponse));
    }

    @GetMapping(value = "/readyplayer/{playername}", produces = "application/json")
    public ResponseEntity<PlayerResponse> ready(@PathVariable("playername") String playerName) {
        PlayerServiceResponse playerServiceResponse = playerService.changePlayerState(playerName, Player.State.READY);
        return ResponseEntity.ok(PlayerResponseTranslator.translate(playerServiceResponse));
    }

    @GetMapping(value = "/actionType/{playerid}/{actionType}", produces = "application/json")
    public ResponseEntity.BodyBuilder play(@PathVariable("playerid") long playerId, @PathVariable("actionType") ActionType actionType) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED);
    }

}