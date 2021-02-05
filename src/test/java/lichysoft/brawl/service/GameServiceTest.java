package lichysoft.brawl.service;

import lichysoft.brawl.model.Player;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    @BeforeEach
    void cleanUp() {
        gameService.restartGame();
    }


    @Test
    void shouldAttack() throws Exception {
        Player test1 = new Player("Warrior");
        Player test2 = new Player("Priest");
        playerService.savePayer(test1);
        playerService.savePayer(test2);
        int hp = test2.getHealth();
        gameService.setPlayerTurn(test1.getID());
        String message = gameService.attack(playerService.findByID(test1.getID()).get().getID(), playerService.findByID(test2.getID()).get().getID());
        test2 = playerService.findByID(test2.getID()).get();
        if (message.contains(" it was a miss ")) {
            assertThat(test2.getHealth() == hp);
        } else {
            assertThat(test2.getHealth() < hp);
        }
    }

    @Test
    void shouldChoosePlayersWithRightData() {
        String s1 = "Hunter";
        String s2 = "Priest";
        assertThat(playerService.findAll()).hasSize(0);
        gameService.choosePlayers(s1, s2);
        assertThat(playerService.findAll()).hasSize(2);
        assertThat(gameService.getPlayerTurn().equals((playerService.findAll()).get(0).getID()) ||
                gameService.getPlayerTurn().equals((playerService.findAll()).get(1).getID()));
    }

    @Test
    void shouldChoosePlayersWithWrongData() {
        String s1 = "Hunterr";
        String s2 = "Priweest";
        // Assertions.assertThrows(Exception.class, () ->gameService.choosePlayers(s1, s2));
        String message = gameService.choosePlayers(s1, s2);
        assertThat(message.contains("Wrong player class"));

    }


    @Test
    void shouldRestartGame() throws Exception {
        Player p1 = new Player("Hunter");
        playerService.savePayer(p1);
        assertThat(playerService.findAll()).isNotEmpty();
        gameService.restartGame();
        assertThat(playerService.findAll()).isEmpty();
    }

    @Test
    void shouldSpecialAction() throws Exception {
        Player test1 = new Player("Rogue");
        Player test2 = new Player("Warrior");
        playerService.savePayer(test1);
        playerService.savePayer(test2);
        int hp = test2.getHealth();
        gameService.setPlayerTurn(test1.getID());
        gameService.specialAction(playerService.findByID(test1.getID()).get().getID(),
                playerService.findByID(test2.getID()).get().getID());
        test2 = playerService.findByID(test2.getID()).get();
        assertThat(test2.getHealth() < hp);

    }

}
