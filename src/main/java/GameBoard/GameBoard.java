package GameBoard;

import Constants.Constants;
import Map.*;

/**
 * 
 */
public class GameBoard {

    private Map gameMap;
    private KillshotTrack track;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    public static GameBoard instance;


    private GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new Map(mapChoice);
        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck(Constants.NUMBER_OF_WEAPONS);
        this.powerupDeck = new PowerupDeck();
    }

    public static GameBoard instance(int mapChoice, int skullChoice){
        if(instance == null) instance = new GameBoard(mapChoice, skullChoice);
        return instance;
    }

    public KillshotTrack getTrack() {
        return this.track;
    }

    /**
     * @return
     */
    public void setup() {
        weaponDeck.shuffle();
        powerupDeck.shuffle();
    }

    public Map getMap(){
        return this.gameMap;
    }

}