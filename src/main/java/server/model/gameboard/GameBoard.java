package server.model.gameboard;

import constants.Constants;
import server.model.map.*;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.Collections;
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

    private List<GameCharacter> gameCharacterList;

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
        this.gameCharacterList = new ArrayList<>();
        //setupGameBoard();

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

    public GameBoard CreateCopy(GameBoard gameBoardToCopy){
        GameBoard gameBoard = new GameBoard();
        gameBoard.gameCharacterList = null;
        gameBoard.gameMap = gameBoardToCopy.gameMap.CreateCopy(gameBoardToCopy.gameMap);
        gameBoard.track = gameBoardToCopy.track.CreateCopy(gameBoardToCopy.track);
        //gameBoard.weaponDeck =
        //gameBoard.powerupDeck =
        //gameBoard.ammoTileDeck =
        return gameBoard;

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
}