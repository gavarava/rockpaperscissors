package com.rps.infrastructure;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  @CrossOrigin
  @GetMapping(value = "/player/{playerName}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity player(@PathVariable("playerName") String playerName) throws RPSException {
    Player player = playerService.getPlayer(playerName);
    return ResponseEntity.ok(player);
  }

  @CrossOrigin
  @PostMapping(value = "/player/{playerName}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity createPlayer(@PathVariable("playerName") String playerName)
      throws RPSException {
    playerService.createPlayer(playerName);
    return ResponseEntity.created(URI.create("/player/" + playerName))
        .body(playerService.getPlayer(playerName));
  }

  @CrossOrigin
  @DeleteMapping(value = "/player/{playerName}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity deletePlayer(@PathVariable("playerName") String playerName)
      throws RPSException {
    playerService.deletePlayer(playerName);
    return ResponseEntity.noContent().build();
  }

  @CrossOrigin
  @PostMapping(value = "/createInvite/{playerName}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity createInvite(@PathVariable("playerName") String inviter)
      throws RPSException {
    Player player = playerService.getPlayer(inviter);
    GameSession session = gameSessionService.createSessionFrom(new Invite(player));
    return ResponseEntity.ok(session);
  }

  @CrossOrigin
  @PostMapping(value = "/acceptInvite/{inviteCode}/{playerName}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity acceptInvite(@PathVariable("inviteCode") String inviteCode,
      @PathVariable("playerName") String playerName)
      throws InvalidOperationException, RPSException {
    Player player = playerService.getPlayer(playerName);
    return ResponseEntity.ok(gameSessionService.acceptInvite(player, inviteCode));
  }

  @CrossOrigin
  @GetMapping(value = "/session/{inviteCode}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity session(@PathVariable("inviteCode") String inviteCode) {
    return ResponseEntity.ok(gameSessionService.sessions().get(inviteCode));
  }

  @CrossOrigin
  @PostMapping(value = "/readyplayer/{playername}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity ready(@PathVariable("playername") String playerName) throws RPSException {
    Player player = playerService.changePlayerState(playerName, Player.State.READY);
    return ResponseEntity.ok(player);
  }

  @CrossOrigin
  @PostMapping(value = "/play", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity play(@RequestBody PlayRequest playRequest) throws RPSException {
    gameplayService.play(playRequest);
    return ResponseEntity.ok().body("");
  }

}