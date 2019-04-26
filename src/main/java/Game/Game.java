package Game;

import Exceptions.*;
import GameBoard.*;
import Player.*;
import Map.*;

import java.util.*;

/**
 * 
 */
public class Game {

    private GameState currentState;

    private GameMap currentGameMap;

    private List<PlayerAbstract> activePlayers;

    private GameBoard currentGameBoard;

    private int currentPlayerIndex;

    private int firstPlayerIndex;

    private WeaponDeck weaponDeck;

    private PowerupDeck powerupDeck;

    public Game(int mapChoice, int skullChoice) {
        this.currentState = GameState.START_MENU;
        this.currentGameBoard = GameBoard.instance(mapChoice,skullChoice);
        this.currentGameMap = currentGameBoard.getMap();
        this.activePlayers = new ArrayList<PlayerAbstract>();
        this.weaponDeck = currentGameBoard.getWeaponDeck();
        this.powerupDeck = currentGameBoard.getPowerupDeck();
        this.currentPlayerIndex = 0;
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

    public void setFirstPlayer(int index) throws WrongGameStateException{
        if(this.currentState == GameState.SETUP)
            this.firstPlayerIndex = index;
        else throw new WrongGameStateException();
    }

    public void nextPlayer() throws WrongGameStateException{
        if(this.currentState == GameState.NORMAL_TURN
                || this.currentState == GameState.FINAL_FRENZY) {
            if (currentPlayerIndex < this.activePlayers.size()) this.currentPlayerIndex++;
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


}