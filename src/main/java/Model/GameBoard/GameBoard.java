package Model.GameBoard;

import Constants.Constants;
import Model.Map.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GameBoard {

    private GameMap gameMap;
    private KillshotTrack track;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    private AmmoTileDeck ammoTileDeck;
    public static GameBoard instance;


    private GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new GameMap(mapChoice);
        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoTileDeck = new AmmoTileDeck();
    }

    public void setupGameBoard(){
        List<SquareAbstract> listOfSquares = new ArrayList<>();
        List<SpawnPoint> listOfSpawnPoints = GameMap.getSpawnPoints();

        for(Room room : GameMap.getRooms()){
            listOfSquares.addAll(room.getSquares());
        }

        listOfSquares.removeAll(listOfSpawnPoints);

        for(SquareAbstract square : listOfSquares){
            square.addItem(this.ammoTileDeck.draw());
        }

        for(SpawnPoint sp : listOfSpawnPoints){
            for(int i = 0; i < Constants.NUMBER_OF_WEAPON_PER_SPAWN_POINT; i++){
                sp.addItem(this.weaponDeck.draw());
            }
        }
    }

    public static GameBoard instance(int mapChoice, int skullChoice){
        if(instance == null) instance = new GameBoard(mapChoice, skullChoice);
        return instance;
    }

    public KillshotTrack getTrack() {
        return this.track;
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

    public AmmoTileDeck getAmmoTileDeck() {
        return ammoTileDeck;
    }
}