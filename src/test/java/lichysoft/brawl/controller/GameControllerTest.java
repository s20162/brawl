package lichysoft.brawl.controller;

import lichysoft.brawl.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameService gameService;

    @Test
    void gameInit() throws Exception {
        mockMvc.perform(post("/game/init")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void choosePlayers() throws Exception {
        mockMvc.perform(post("/game/players/Priest/Rogue"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Turn of player with ID: ")));
    }

    @Test
    void gameReset() throws Exception {
        mockMvc.perform(post("/game/restart"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(equalTo("Your game was restarted, choose new players")));
    }

    @Test
    void attackPlayer() throws Exception {
        gameService.choosePlayers("Priest", "Warrior");
        Long turn = gameService.getPlayerTurn();
        Long turn2;
        if (turn == 1L) {
            turn2 = 2L;
        } else {
            turn2 = 1L;
        }
        mockMvc.perform(post("/game/attack/" + turn + "/" + turn2))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("You hit for: ")));
    }

    @Test
    void specialAction() {
    }
}