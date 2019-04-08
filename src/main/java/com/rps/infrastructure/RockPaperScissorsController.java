package com.rps.infrastructure;

import com.rps.application.GameSessionService;
import com.rps.application.GameplayService;
import com.rps.application.RPSException;
import com.rps.application.players.PlayerService;
import com.rps.domain.actors.Player;
import com.rps.domain.gameplay.GameSession;
import com.rps.domain.gameplay.Invite;
import com.rps.domain.gameplay.exceptions.InvalidOperationException;
import java.net.URI;
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
  private GameSessionService gameSessionService;

  @Autowired
  private GameplayService gameplayService;

  @GetMapping(value = "/player/{playerName}")
  public ResponseEntity player(@PathVariable("playerName") String playerName) {
    try {
      Player player = playerService.getPlayer(playerName);
      return ResponseEntity.ok(player);
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/player/{playerName}")
  public ResponseEntity createPlayer(@PathVariable("playerName") String playerName) {
    try {
      playerService.createPlayer(playerName);
      return ResponseEntity.created(URI.create("/player/" + playerName))
          .body(playerService.getPlayer(playerName));
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  @DeleteMapping(value = "/player/{playerName}")
  public ResponseEntity deletePlayer(@PathVariable("playerName") String playerName) {
    try {
      playerService.deletePlayer(playerName);
      return ResponseEntity.noContent().build();
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/createInvite/{playerName}")
  public ResponseEntity createInvite(@PathVariable("playerName") String inviter) {
    try {
      Player player = playerService.getPlayer(inviter);
      GameSession session = gameSessionService.createSessionFrom(new Invite(player));
      return ResponseEntity.ok(session);
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}")
  public ResponseEntity acceptInvite(@PathVariable("inviteCode") String inviteCode,
      @PathVariable("playerName") String playerName) throws InvalidOperationException {
    try {
      Player player = playerService.getPlayer(playerName);
      return ResponseEntity.ok(gameSessionService.acceptInvite(player, inviteCode));
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping(value = "/session/{inviteCode}")
  public ResponseEntity session(@PathVariable("inviteCode") String inviteCode) {
    return ResponseEntity.ok(gameSessionService.sessions().get(inviteCode));
  }

  @PostMapping(value = "/readyplayer/{playername}")
  public ResponseEntity ready(@PathVariable("playername") String playerName) {
    try {
      Player player = playerService.changePlayerState(playerName, Player.State.READY);
      return ResponseEntity.ok(player);
    } catch (RPSException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/play")
  public ResponseEntity play(@RequestBody PlayRequest playRequest) {
    try {
      gameplayService.play(playRequest);
      return ResponseEntity.ok().body("");
    } catch (RPSException e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
  }

}