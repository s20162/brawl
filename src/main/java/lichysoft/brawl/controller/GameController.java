package lichysoft.brawl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lichysoft.brawl.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/init")
    public ResponseEntity<String> gameInit() {
        return ResponseEntity.ok("Your game was initialized \n" +
                "to start the game choose players \navailable classes are:\n" +
                "Warrior - to see info type '/player/WarriorInfo'\n" +
                "Rogue - to see info type '/player/RogueInfo'\n" +
                "Priest - to see info type '/player/PriestInfo'\n" +
                "Hunter - to see info type '/player/HunterInfo'\n" +
                "to choose players type '/game/players/FirstPlayerClass/SecondPlayerClass");
    }

    @PostMapping("/players/{playerOneClass}/{playerTwoClass}")
    public ResponseEntity<String> choosePlayers(@PathVariable String playerOneClass, @PathVariable String playerTwoClass) {
        return ResponseEntity.ok(gameService.choosePlayers(playerOneClass, playerTwoClass) +
                "\n ") ;

    }

    @PostMapping("/restart")
    public ResponseEntity<String> gameReset() {
        return ResponseEntity.ok(gameService.restartGame());
    }

    @PostMapping("/attack/{playerOneID}/{playerTwoID}")
    public ResponseEntity<String> attackPlayer(@PathVariable Long playerOneID, @PathVariable Long playerTwoID) throws JsonProcessingException {
//        Player returnedPlayer = gameService.attack(playerOneID, playerTwoID);
        String status = gameService.attack(playerOneID, playerTwoID);

        if (status == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(status);
        }
    }

    @PostMapping("/specialAction/{playerOneID}/{playerTwoID}")
    public ResponseEntity<String> specialAction(@PathVariable Long playerOneID, @PathVariable Long playerTwoID) throws JsonProcessingException {
//        Player returnedPlayer = gameService.attack(playerOneID, playerTwoID);
        String status = gameService.specialAction(playerOneID, playerTwoID);

        if (status == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(status);
        }
    }
}
