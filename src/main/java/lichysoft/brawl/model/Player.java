package lichysoft.brawl.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Player {


    @Id
    @GeneratedValue
    private Long ID;
    private String nickName;
    private int health;
    private int manaPoints = 30;
    private int minAttack;
    private int maxAttack;


    public Player() {
    }

    public Player(Long ID) {
        this.ID = ID;
    }

    public Player(String type) throws Exception {
        switch (type) {
            case "Hunter":
                this.nickName = "Hunter";
                this.health = 1000;
                this.minAttack = 100;
                this.maxAttack = 350;
                break;
            case "Warrior":
                this.nickName = "Warrior";
                this.health = 1500;
                this.minAttack = 150;
                this.maxAttack = 250;
                break;
            case "Priest":
                this.nickName = "Priest";
                this.health = 1000;
                this.minAttack = 100;
                this.maxAttack = 200;
                break;
            case "Rogue":
                this.nickName = "Rogue";
                this.health = 800;
                this.minAttack = 300;
                this.maxAttack = 350;
                break;
            default:
                throw new Exception();

        }
    }

    public int getMaxManaPoints() {
        return 30;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public void setManaPoints(int manaPoints) {
        this.manaPoints = manaPoints;
    }

    public int getMinAttack() {
        return minAttack;
    }

    public void setMinAttack(int minAttack) {
        this.minAttack = minAttack;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    public void setMaxAttack(int maxAttack) {
        this.maxAttack = maxAttack;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ID:\"" + ID +
                ", \"nickName\":'" + nickName + '\'' +
                ", \"health\":" + health +
                ", \"manaPoints\":" + manaPoints +
                ", \"minAttack\":" + minAttack +
                ", \"maxAttack\":" + maxAttack +
                '}';
    }
}