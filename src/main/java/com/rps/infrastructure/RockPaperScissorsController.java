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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@RestController
public class RockPaperScissorsController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private GameSessionService gameSessionService;

    public RockPaperScissorsController(PlayerService playerService, GameSessionService gameSessionService) {
        this.playerService = playerService;
        this.gameSessionService = gameSessionService;
    }

    @RequestMapping(value = "/player/{playerName}", produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity player(@PathVariable("playerName") String playerName) {
        try {
            if (GET.name().equals(context.getMethod())) {
                Player player = playerService.getPlayer(playerName);
                return ResponseEntity.ok(player);
            } else if (POST.name().equals(context.getMethod())) {
                playerService.createPlayer(playerName);
                return ResponseEntity.ok().body("");
            } else {
                return ResponseEntity.badRequest().allow(GET, POST).body("");
            }
        } catch (RPSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/ping", produces = "application/json")
    public DefaultResponse ping() {
        return new DefaultResponse("pong", "TEST");
    }

    @PostMapping(value = "/createInvite/{playerName}", produces = "application/json")
    public ResponseEntity createInvite(@PathVariable("playerName") String playerName) {
        try {
            Player player = playerService.getPlayer(playerName);
            GameSession session = gameSessionService.createSessionFrom(new Invite(player));
            return ResponseEntity.ok(session);
        } catch (RPSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}", produces = "application/json")
    public ResponseEntity acceptInvite(@PathVariable("inviteCode") String inviteCode, @PathVariable("playerName") String playerName) throws InvalidOperationException {
        Player player = null;
        try {
            player = playerService.getPlayer(playerName);
        } catch (RPSException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(gameSessionService.acceptInvite(player, inviteCode));
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
    public ResponseEntity play(@PathVariable("playerid") long playerId, @PathVariable("actionType") ActionType actionType) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
    }

}