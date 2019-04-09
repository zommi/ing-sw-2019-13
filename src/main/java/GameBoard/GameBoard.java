package GameBoard;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import Constants.Constants;
import Map.Map;

/**
 * 
 */
public class GameBoard {

    private Map gameMap;
    private KillshotTrack track;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    public static GameBoard istance;


    private GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new Map(mapChoice);

        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck(Constants.NUMBER_OF_WEAPONS);
        this.powerupDeck = new PowerupDeck();
    }

    public static GameBoard istance(int mapChoice, int skullChoice){
        if(istance != null) istance = new GameBoard(mapChoice, skullChoice);
        return istance;
    }


    /**
     * @return
     */
    public void setup() {
        weaponDeck.shuffle();
        powerupDeck.shuffle();
    }

}