package lichysoft.brawl.service;

import lichysoft.brawl.model.Player;
import lichysoft.brawl.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void savePayer(Player player) {
        playerRepository.save(player);
    }

    public Optional<Player> findByID(Long playerID) {
        return playerRepository.findById(playerID);
    }

    public int calculatedDamage(Player player) {
        Random rand = new Random();
        int damage = rand.nextInt((player.getMaxAttack() - player.getMinAttack()) - 1) + player.getMinAttack();
        if (damage>= player.getMaxAttack()*0.95) {
            return (int) (damage*1.3);
        } else if (damage<player.getMinAttack()*1.05) {
            return 0;
        }else{
            return damage;
        }

    }

    public void deletePlayerByID(Long ID){
        playerRepository.deleteById(ID);
    }

    public void deleteAllPlayers(){
        playerRepository.deleteAll();
    }

    public String getRogueInfo() {
        return "Class Rogue info: \nbase stats: \n" +
                "min attack power:300 \n" +
                "max attack power: 350 \n" +
                "health points:800 \n" +
                "special action: Triple hit, cost 10mp \n" +
                "description: You have a normal chance to hit 3 times";
    }

    public String getPriestInfo() {
        return "Class Priest info: \nbase stats: \n" +
                "min attack power:100 \n" +
                "max attack power: 200 \n" +
                "health points:1000 \n" +
                "special action: Life steal, cost 10mp \n" +
                "description: you steal 200 hp from opponent and heal yourself for twice the amount";
    }

    public String getWarriorInfo() {
        return "Class Warrior info: \nbase stats: \n" +
                "min attack power:150 \n" +
                "max attack power: 250 \n" +
                "health points:1500 \n" +
                "special action: Rage, cost 10mp \n" +
                "description: Rage consumes 200 of yours hp but gives you boost of 300 dp for this turn";
    }

    public String getHunterInfo() {
        return "Class Hunter info: \nbase stats: \n" +
                "min attack power:100 \n" +
                "max attack power: 350 \n" +
                "health points:1000 \n" +
                "special action: Power shot, cost 10mp \n" +
                "description: You have a 15% chance to hit for 1000 damage";
    }


}