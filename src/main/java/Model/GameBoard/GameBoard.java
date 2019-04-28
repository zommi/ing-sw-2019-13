package Model.GameBoard;

import Constants.Constants;
import Model.Map.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing all the different physical elements of a certain game.
 */
public class GameBoard {

    /**
     * reference to the game map
     */
    private GameMap gameMap;

    /**
     * reference to the killshot track
     */
    private KillshotTrack track;

    /**
     * reference to the weapon deck
     */
    private WeaponDeck weaponDeck;

    /**
     * reference to the powerups deck
     */
    private PowerupDeck powerupDeck;

    /**
     * reference to the AmmoTile deck
     */
    private AmmoTileDeck ammoTileDeck;

    /**
     * reference to the itself
     */
    public static GameBoard instance;


    /**
     * Constructor called by the instance method
     * @param mapChoice index of the map chosen by the players in the setup phase
     * @param skullChoice number of skulls
     */
    private GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new GameMap(mapChoice);
        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoTileDeck = new AmmoTileDeck();
    }

    /**
     * Method that puts an AmmoTile on every square and three weapons on
     * every spawnpoint
     */
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

    /**
     * Method that returns the instance of the GameBoard if it exists, else it creates a new
     * instance
     * @param mapChoice int containing the index of the map chosen
     * @param skullChoice int containing the number of skulls of the game
     * @return the instance of GameBoard
     */
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