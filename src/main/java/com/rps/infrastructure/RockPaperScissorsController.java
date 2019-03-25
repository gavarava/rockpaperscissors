package com.rps.infrastructure;

import com.rps.application.GameSessionService;
import com.rps.application.RPSException;
import com.rps.application.players.PlayerService;
import com.rps.domain.actors.Player;
import com.rps.domain.actors.Player.State;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.InvalidOperationException;
import com.rps.domain.gameplay.Invite;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RockPaperScissorsController {

  @Autowired
  private PlayerService playerService;

  @Autowired
  private HttpServletRequest context;

  @Autowired
  private GameSessionService gameSessionService;

  public RockPaperScissorsController(PlayerService playerService,
      GameSessionService gameSessionService) {
    this.playerService = playerService;
    this.gameSessionService = gameSessionService;
  }

  @GetMapping(value = "/ping", produces = "application/json")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("{\"response\":\"pong\"}");
  }

  @GetMapping(value = "/player/{playerName}", produces = "application/json")
  public ResponseEntity player(@PathVariable("playerName") String playerName) {
    try {
      Player player = playerService.getPlayer(playerName);
      return ResponseEntity.ok(player);
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/player/{playerName}", produces = "application/json")
  public ResponseEntity playerPOST(@PathVariable("playerName") String playerName) {
    try {
      playerService.createPlayer(playerName);
      return ResponseEntity.ok().body("");
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  @DeleteMapping(value = "/player/{playerName}", produces = "application/json")
  public ResponseEntity playerDELETE(@PathVariable("playerName") String playerName) {
    try {
      playerService.deletePlayer(playerName);
      return ResponseEntity.ok("");
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/createInvite/{playerName}", produces = "application/json")
  public ResponseEntity createInvite(@PathVariable("playerName") String inviter) {
    try {
      Player player = playerService.getPlayer(inviter);
      GameSession session = gameSessionService.createSessionFrom(new Invite(player));
      return ResponseEntity.ok(session);
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}", produces = "application/json")
  public ResponseEntity acceptInvite(@PathVariable("inviteCode") String inviteCode,
      @PathVariable("playerName") String playerName) throws InvalidOperationException {
    try {
      Player player = playerService.getPlayer(playerName);
      return ResponseEntity.ok(gameSessionService.acceptInvite(player, inviteCode));
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping(value = "/session/{inviteCode}", produces = "application/json")
  public ResponseEntity session(@PathVariable("inviteCode") String inviteCode) {
    return ResponseEntity.ok(gameSessionService.sessions().get(inviteCode));
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

  @PostMapping(value = "/play", produces = "application/json")
  public ResponseEntity play(@RequestBody Action action) {
    try {
      Player player = playerService.changePlayerState(action.getPlayerName(), State.PLAYING);
      GameSession currentSession = gameSessionService.sessions().get(action.getInviteCode());
      currentSession.changeStateTo(GameSession.State.PLAYING);
      return ResponseEntity.ok().body("");
    } catch (RPSException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
  }

}