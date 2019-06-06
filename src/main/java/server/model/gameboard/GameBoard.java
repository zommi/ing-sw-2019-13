package server.model.gameboard;

import client.weapons.Weapon;
import constants.Constants;
import server.model.cards.AmmoTile;
import server.model.cards.WeaponCard;
import server.model.map.*;
import server.model.player.ConcretePlayer;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import java.util.HashMap;


import java.io.Serializable;
import java.util.*;

/**
 * Class containing all the different physical elements of a certain game.
 */
public class GameBoard implements Serializable {

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
    private transient WeaponDeck weaponDeck;

    private List<String> listOfPlayerNames;

    /**
     * reference to the powerups deck
     */
    private transient PowerupDeck powerupDeck;

    /**
     * reference to the AmmoTile deck
     */
    private transient AmmoTileDeck ammoTileDeck;

    private Map<Integer,PlayerBoard> mapPlayerBoard;

    private List<GameCharacter> gameCharacterList = new ArrayList<>();

    public GameBoard(){

    }

    /**
     * Constructor called by the instance method
     * @param mapChoice index of the map chosen by the players in the setup phase
     * @param skullChoice number of skulls
     */
    public GameBoard(int mapChoice, int skullChoice) {
        this.gameMap = new GameMap(mapChoice);
        this.track = new KillshotTrack(skullChoice);
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoTileDeck = new AmmoTileDeck();
        this.mapPlayerBoard = new HashMap<>();
        setupGameBoard();

    }

    public List<String> getPlayerNames(){
        return this.listOfPlayerNames;
    }

    public void setPlayerNames(List<String> string){
        this.listOfPlayerNames = string;
    }

    public AmmoTile drawAmmo(){
        return this.ammoTileDeck.draw();
    }

    public WeaponCard drawWeapon(){
        return this.weaponDeck.draw();
    }

    public Map getHashMap(){
        return this.mapPlayerBoard;
    }

    /**
     * Method that puts an AmmoTile on every square and three weapons on
     * every spawnpoint
     */
    public void setupGameBoard(){
        List<SquareAbstract> listOfSquares = new ArrayList<>();
        List<SpawnPoint> listOfSpawnPoints = gameMap.getSpawnPoints();

        for(Room room : gameMap.getRooms()){
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

    public void addPlayerBoard(ConcretePlayer p){
        this.mapPlayerBoard.put(p.getClientID(), p.getBoard());
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

    public List<GameCharacter> getGameCharacterList() {
        return gameCharacterList;
    }

    public void addGameCharacter(GameCharacter gameCharacter){
        this.gameCharacterList.add(gameCharacter);
    }

    public Weapon getWeapon(String name){       //TODO
        return weaponDeck.getWeapon(name);
    }
}