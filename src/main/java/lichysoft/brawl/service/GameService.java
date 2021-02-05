package lichysoft.brawl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lichysoft.brawl.model.Player;
import lichysoft.brawl.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class GameService {
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    ObjectMapper mapper = new ObjectMapper();
    Long playerTurn;

    public Long getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Long playerTurn) {
        this.playerTurn = playerTurn;
    }

    public GameService(PlayerService playerService, PlayerRepository playerRepository) {
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

    public String attack(Long attackerID, Long defenderID) throws JsonProcessingException {
        if (attackerID.equals(defenderID)) {
            return "You can't hit yourself\nturn of player with ID: " + playerTurn;
        }
        Optional<Player> attacker = playerService.findByID(attackerID);
        Optional<Player> defender = playerService.findByID(defenderID);
        String message;
        if (attacker.isPresent() && defender.isPresent()) {
            if (!attacker.get().getID().equals(playerTurn))
                return "Wrong players is trying to attack, turn of player with ID: " + playerTurn;
            if (attacker.get().getHealth() < 0 || defender.get().getHealth() < 0) {
                return "One player is dead, start a new game ";
            }
            int damage = playerService.calculatedDamage(attacker.get());
            defender.get().setHealth(defender.get().getHealth() - damage);
            defender.get().setManaPoints(defender.get().getManaPoints() + 5);
            if (defender.get().getManaPoints() > defender.get().getMaxManaPoints()) {
                defender.get().setManaPoints(30);
            }
            attacker.get().setManaPoints(attacker.get().getManaPoints() + 5);
            if (attacker.get().getManaPoints() > attacker.get().getMaxManaPoints()) {
                attacker.get().setManaPoints(30);
            }
            playerService.savePayer(defender.get());
            playerService.savePayer(attacker.get());

            Random rand = new Random();
            boolean ifFirst = rand.nextBoolean();

            message = "You hit for: " + damage;
            if (damage < attacker.get().getMinAttack()) {
                message += " it was a miss ";
            } else if (damage > attacker.get().getMaxAttack()) {
                message += " it was a critical hit! ";
            }
            message = checkTurn(attacker, defender, message, ifFirst);
            return message + " \nplayers status \n" + mapper.writeValueAsString(playerRepository.findById(attackerID).orElseThrow()) +
                    mapper.writeValueAsString(playerRepository.findById(defenderID).orElseThrow());
        } else
            return null;
    }

    public String choosePlayers(String playerOneClass, String playerTwoClass) {
        try {
            Player playerOne = new Player(playerOneClass);
            playerRepository.save(playerOne);

            Player playerTwo = new Player(playerTwoClass);
            playerRepository.save(playerTwo);

            Random rand = new Random();
            boolean ifFirst = rand.nextBoolean();
            if (ifFirst)
                playerTurn = playerOne.getID();
            else
                playerTurn = playerTwo.getID();
            return "Turn of player with ID: " + playerTurn + "\n" +
                    mapper.writeValueAsString(playerRepository.findById(playerOne.getID()).orElse(null)) +
                    mapper.writeValueAsString(playerRepository.findById(playerTwo.getID()).orElse(null));
        } catch (Exception e) {
            return "Wrong player class";
        }
    }

    public String restartGame() {
        playerService.deleteAllPlayers();
        return "Your game was restarted, choose new players";

    }

    public String specialAction(Long attackerID, Long defenderID) throws JsonProcessingException {
        if (attackerID.equals(defenderID)) {
            return "You can't hit yourself\nturn of player with ID: " + playerTurn;
        }
        Optional<Player> attacker = playerService.findByID(attackerID);
        Optional<Player> defender = playerService.findByID(defenderID);
        String message = null;
        if (attacker.isPresent() && defender.isPresent()) {
            if (!attacker.get().getID().equals(playerTurn)) {
                return "Wrong players is trying to perform special action, turn of player with ID: " + playerTurn;
            }
            if (attacker.get().getHealth() < 0 || defender.get().getHealth() < 0) {
                return "One player is dead, start a new game ";
            }
            if (attacker.get().getManaPoints() < 10) {
                return "not enough mana points, try to attack instead ";
            }
            String type = attacker.get().getNickName();
            switch (type) {
                case "Hunter":
                    attacker.get().setManaPoints(attacker.get().getManaPoints() - 10);
                    double chance = Math.random() * 100;
                    message = "You performed special action \n";
                    if (chance > 85) {
                        defender.get().setHealth(defender.get().getHealth() - 800);
                        message += "You hit for: " + 800;
                    } else {
                        message += "It was a miss";
                    }
                    break;
                case "Warrior":
                    attacker.get().setManaPoints(attacker.get().getManaPoints() - 10);
                    attacker.get().setHealth(attacker.get().getHealth() - 200);
                    if (attacker.get().getHealth() <= 0) {
                        message = "You used up all your hp and died\nstart a new game";
                        return message + " \nplayers status \n" +
                                mapper.writeValueAsString(playerRepository.findById(attackerID).orElseThrow()) +
                                mapper.writeValueAsString(playerRepository.findById(defenderID).orElseThrow());
                    } else {
                        int damage = playerService.calculatedDamage(attacker.get()) + 300;
                        defender.get().setHealth(defender.get().getHealth() - damage);
                        message = "You performed special action \nYou lost 200 hp and hit for:" + damage;
                    }
                    break;
                case "Priest":
                    attacker.get().setManaPoints(attacker.get().getManaPoints() - 10);
                    int damage, heal;
                    damage = Math.min(defender.get().getHealth(), 200);
                    defender.get().setHealth(defender.get().getHealth() - damage);
                    heal = attacker.get().getHealth();
                    attacker.get().setHealth(attacker.get().getHealth() + 2 * damage);
                    if (attacker.get().getHealth() >= 1000) {
                        attacker.get().setHealth(1000);
                    }
                    heal = attacker.get().getHealth() - heal;
                    message = "You performed special action \nYou dealt " + damage +
                            " damage and healed yourself for " + heal;

                    playerService.savePayer(defender.get());
                    playerService.savePayer(attacker.get());
                    break;
                case "Rogue":
                    attacker.get().setManaPoints(attacker.get().getManaPoints() - 10);
                    int damage1, damage2, damage3, damageTotal;
                    String m1;
                    damage1 = playerService.calculatedDamage(attacker.get());
                    damage2 = playerService.calculatedDamage(attacker.get());
                    damage3 = playerService.calculatedDamage(attacker.get());
                    damageTotal = damage1 + damage2 + damage3;
                    defender.get().setHealth(defender.get().getHealth() - damageTotal);

                    m1 = "\nYou hit for: " + damage1;
                    if (damage1 < attacker.get().getMinAttack()) {
                        m1 += " it was a miss ";
                    } else if (damage1 > attacker.get().getMaxAttack()) {
                        m1 += " it was a critical hit! ";
                    }
                    m1 += "\nYou hit for: " + damage2;
                    if (damage2 < attacker.get().getMinAttack()) {
                        m1 += " it was a miss ";
                    } else if (damage2 > attacker.get().getMaxAttack()) {
                        m1 += " it was a critical hit! ";
                    }
                    m1 += "\nYou hit for: " + damage3;
                    if (damage3 < attacker.get().getMinAttack()) {
                        m1 += " it was a miss ";
                    } else if (damage3 > attacker.get().getMaxAttack()) {
                        m1 += " it was a critical hit! ";
                    }

                    message = "You performed special action" + m1 + "\nTotal damage " + damageTotal;
                    break;
            }
            defender.get().setManaPoints(defender.get().getManaPoints() + 5);
            if (defender.get().getManaPoints() > defender.get().getMaxManaPoints()) {
                defender.get().setManaPoints(defender.get().getMaxManaPoints());
            }
            playerService.savePayer(defender.get());
            playerService.savePayer(attacker.get());
            Random rand = new Random();
            boolean ifFirst = rand.nextBoolean();
            message = checkTurn(attacker, defender, message, ifFirst);
            return message + " \nplayers status \n" +
                    mapper.writeValueAsString(playerRepository.findById(attackerID).orElseThrow()) +
                    mapper.writeValueAsString(playerRepository.findById(defenderID).orElseThrow());
        } else
            return null;
    }

    private String checkTurn(Optional<Player> attacker, Optional<Player> defender, String message, boolean ifFirst) {
        if (ifFirst) {
            playerTurn = attacker.orElseThrow().getID();
        } else {
            playerTurn = defender.orElseThrow().getID();
        }
        message += "\nTurn of player with ID: " + playerTurn;
        if (defender.orElseThrow().getHealth() > 0) {
            message += "\nstart next turn";
        } else {
            message += "\nattacking player has won";
        }
        return message;
    }


}