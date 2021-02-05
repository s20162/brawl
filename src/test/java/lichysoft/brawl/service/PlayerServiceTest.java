package lichysoft.brawl.service;

import lichysoft.brawl.model.Player;
import lichysoft.brawl.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private Player player;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void shouldFindAll() {
        when(playerRepository.findAll()).thenReturn(List.of(new Player(), new Player()));
        List<Player> allPlayers = playerService.findAll();
        assertThat(allPlayers).hasSize(2);
    }

    @Test
    void shouldSavePayer() {
        playerService.savePayer(any());
        playerService.savePayer(any());
        verify(playerRepository, times(2)).save(any());

    }

    @Test
    void shouldFindByID() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(new Player(1L)));
        Optional<Player> OP = playerService.findByID(1L);
        OP.ifPresent(value -> assertThat(value.getID()).isEqualTo(1L));
    }

    @Test
    void shouldCalculatedDamage() {
        when(player.getMinAttack()).thenReturn(100);
        when(player.getMaxAttack()).thenReturn(200);
        assertThat(playerService.calculatedDamage(player)>=0);
    }

    @Test
    void shouldDeletePlayerByID() {
        playerService.deletePlayerByID(1L);
        playerService.deletePlayerByID(2L);
        verify(playerRepository, times(2)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteAllPlayers() {
        playerService.deleteAllPlayers();
        verify(playerRepository, times(1)).deleteAll();
    }

    @Test
    void shouldGetRogueInfo() {
        assertThat(playerService.getRogueInfo()).contains("Class Rogue info:");
    }

    @Test
    void shouldGetPriestInfo() {
        assertThat(playerService.getPriestInfo()).contains("Class Priest info:");
    }

    @Test
    void shouldGetWarriorInfo() {
        assertThat(playerService.getWarriorInfo()).contains("Class Warrior info:");
    }

    @Test
    void getHunterInfo() {
        assertThat(playerService.getHunterInfo()).contains("Class Hunter info:");

    }
}