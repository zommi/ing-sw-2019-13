package server.model.game;

import client.weapons.Weapon;
import exceptions.*;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.cards.WeaponCard;
import server.model.gameboard.*;
import server.model.player.*;
import server.model.map.*;
import view.ListOfWeaponsAnswer;

import java.util.*;

/**
 * 
 */
public class Game {

    private TurnHandler turnHandler;

    private TurnPhase currentTurn;

    private GameState currentState;

    private GameMap currentGameMap;

    private List<PlayerAbstract> activePlayers;

    private GameBoard currentGameBoard;

    private int currentPlayerIndex;

    private int firstPlayerIndex;

    private WeaponDeck weaponDeck;

    private PowerupDeck powerupDeck;

    private ListOfWeaponsAnswer weaponList = new ListOfWeaponsAnswer();

    private AmmoTileDeck ammoTileDeck;

    private Map<Integer, PlayerAbstract> clientToPlayer;
    private Map<PlayerAbstract, Integer> playerToClient;


    public Game(int mapChoice, int skullChoice) {
        this.currentState = GameState.SETUP;
        this.currentGameBoard = new GameBoard(mapChoice,skullChoice);
        this.currentGameMap = currentGameBoard.getMap();
        this.activePlayers = new ArrayList<>();
        this.weaponDeck = currentGameBoard.getWeaponDeck();
        this.powerupDeck = currentGameBoard.getPowerupDeck();
        this.ammoTileDeck = currentGameBoard.getAmmoTileDeck();
        this.currentPlayerIndex = 0;
        this.turnHandler = new TurnHandler();
        for(int i = 0; i < weaponDeck.getSize(); i++){
            WeaponCard temp = weaponDeck.getWeaponFromIndex(i);
            this.weaponList.add(temp);
            //System.out.println("added the weapon " +weaponDeck.getWeaponFromIndex(i).getName());
        }
    }

    public ListOfWeaponsAnswer getWeaponList(){
        return this.weaponList;
    }

    public TurnPhase getCurrentTurn(){
        return this.currentTurn;
    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }


    public void addPlayer(PlayerAbstract player) throws WrongGameStateException {
        if (this.currentState == GameState.SETUP) {
            this.activePlayers.add(player);
        }else throw new WrongGameStateException();
    }

    public void removePlayer(PlayerAbstract player) throws WrongGameStateException{
        if(this.currentState == GameState.SETUP){
            this.activePlayers.remove(player);
        }else throw new WrongGameStateException();
    }

    public PlayerAbstract getCurrentPlayer(){
        return this.activePlayers.get(currentPlayerIndex);
    }

    public void setFirstPlayer(int index) throws WrongGameStateException{
        if(this.currentState == GameState.SETUP)
            this.firstPlayerIndex = index;
        else throw new WrongGameStateException();
    }

    public void nextPlayer() throws WrongGameStateException{
        if(this.currentState == GameState.NORMAL_TURN
                || this.currentState == GameState.FINAL_FRENZY) {
            if (currentPlayerIndex < this.activePlayers.size() - 1) this.currentPlayerIndex++;
            else this.currentPlayerIndex = 0;
        }else throw new WrongGameStateException();
    }


    public void nextState() throws WrongGameStateException{
        switch (this.currentState){
            case START_MENU:
                this.currentState = GameState.SETUP;
                break;
            case SETUP:
                this.currentState = GameState.NORMAL_TURN;
                break;
            case NORMAL_TURN:
                this.currentState = GameState.FINAL_FRENZY;
                break;
            case FINAL_FRENZY:
                this.currentState = GameState.END_GAME;
                break;
            case END_GAME:
                this.currentState = GameState.START_MENU;
                break;
            default:
                throw new WrongGameStateException();
        }
    }


    public GameBoard getCurrentGameBoard() {
        return currentGameBoard;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getFirstPlayerIndex() {
        return firstPlayerIndex;
    }

    public List<PlayerAbstract> getActivePlayers() {
        return activePlayers;
    }

    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }

    public PowerupDeck getPowerupDeck() {
        return powerupDeck;
    }

    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    public AmmoTileDeck getAmmoTileDeck() {
        return ammoTileDeck;
    }
}