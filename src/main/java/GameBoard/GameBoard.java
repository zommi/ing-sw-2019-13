package GameBoard;

import Map.*;

/**
 * 
 */
public class GameBoard {

    private GameMap gameMap;
    private KillshotTrack track;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    public static GameBoard instance;


    private GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new GameMap(mapChoice);
        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
    }

    public static GameBoard instance(int mapChoice, int skullChoice){
        if(instance == null) instance = new GameBoard(mapChoice, skullChoice);
        return instance;
    }

    public KillshotTrack getTrack() {
        return this.track;
    }

    //set up ammotiles in squares and weapons in spawnpoints
    public void setup() {

    }

    public GameMap getMap(){
        return this.gameMap;
    }

    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    public PowerupDeck getPowerupDeck() {
        return powerupDeck;
    }
}