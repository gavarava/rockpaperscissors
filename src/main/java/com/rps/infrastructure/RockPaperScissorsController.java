package com.rps.infrastructure;

import com.rps.application.players.PlayerCreationDetails;
import com.rps.application.players.PlayerService;
import com.rps.domain.gameplay.ActionType;
import com.rps.infrastructure.players.PlayerCreationResponse;
import com.rps.infrastructure.players.PlayerResponseTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
public class RockPaperScissorsController {

    @Autowired
    private PlayerService playerService;

    public RockPaperScissorsController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = "application/json")
    public DefaultResponse ping() {
        return new DefaultResponse("pong", "TEST");
    }

    @RequestMapping(value = "/register/{nickname}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<PlayerCreationResponse> registerPlayer(@PathVariable("nickname") String nickname) {
        PlayerCreationDetails playerCreationDetails = playerService.createPlayerWithName(nickname);
        return ResponseEntity.ok(PlayerResponseTranslator.translate(playerCreationDetails));
    }

    @RequestMapping(value = "/check/{playerid}", method = RequestMethod.GET, produces = "application/json")
    public Response checkGame(@PathVariable("playerid") Long playerId) {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/ready/{playerid}", method = RequestMethod.GET, produces = "application/json")
    public Response ready(@PathVariable("playerid") long playerid) {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/actionType/{playerid}/{actionType}", method = RequestMethod.GET, produces = "application/json")
    public Response play(@PathVariable("playerid") long playerId, @PathVariable("actionType") ActionType actionType) {
        throw new NotImplementedException();
    }

}