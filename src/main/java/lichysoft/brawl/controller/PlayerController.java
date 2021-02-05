package lichysoft.brawl.controller;

import lichysoft.brawl.model.Player;
import lichysoft.brawl.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> returnAllPlayers() {
        return ResponseEntity.ok(playerService.findAll());
    }

   /* @PostMapping
    public ResponseEntity<Player> savePlayer(@RequestBody Player player) {

        return ResponseEntity.ok(playerService.savePayer(player));

    }*/

    @PostMapping("/delete/{playerID}")
    public String deletePlayer(@PathVariable Long playerID) {
        playerService.deletePlayerByID(playerID);
        return "Deleted";
    }
    @PostMapping("/RogueInfo")
    public String rogueInfo() {
        return playerService.getRogueInfo();
    }

    @PostMapping("/PriestInfo")
    public String priestInfo() {
        return playerService.getPriestInfo();
    }

    @PostMapping("/WarriorInfo")
    public String warriorInfo() {
        return playerService.getWarriorInfo();
    }

    @PostMapping("/HunterInfo")
    public String hunterInfo() {
        return playerService.getHunterInfo();
    }

}