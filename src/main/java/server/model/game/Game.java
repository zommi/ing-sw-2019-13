package server.model.game;

import constants.Color;
import server.controller.Controller;
import server.controller.TurnHandler;
import server.model.cards.AmmoTile;
import server.model.cards.WeaponCard;
import server.model.gameboard.*;
import server.model.player.*;
import server.model.map.*;

import java.util.*;

/**
 * 
 */
public class Game {

    private Controller controller;

    private TurnHandler turnHandler;

    private GameState currentState;

    private GameMap currentGameMap;

    private List<PlayerAbstract> players;

    private GameBoard currentGameBoard;

    private int currentPlayerIndex;

    private int lastPlayerIndex;



    public Game(int mapChoice, int skullChoice, Controller controller) {
        this.currentState = GameState.SETUP;
        this.currentGameBoard = new GameBoard(mapChoice,skullChoice);
        this.currentGameMap = currentGameBoard.getMap();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.lastPlayerIndex = 0;
        this.controller = controller;
        this.turnHandler = new TurnHandler(controller);
        this.currentGameBoard.setGameState(currentState);
    }

    public Controller getController() {
        return controller;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
        this.currentGameBoard.setGameState(currentState);
    }

    public AmmoTile drawAmmo(){
        return this.currentGameBoard.drawAmmo();
    }

    public WeaponCard drawWeapon(){
        return this.currentGameBoard.drawWeapon();
    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public void addPlayerToGame(PlayerAbstract player) {
        this.players.add(player);
        this.currentGameBoard.addPlayerBoard((ConcretePlayer) player);
    }

    public void removePlayer(PlayerAbstract player){
        players.remove(player);
    }

    public PlayerAbstract getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public int nextPlayer(){
        lastPlayerIndex = currentPlayerIndex;
        do{
            if (currentPlayerIndex < this.players.size() - 1)
                currentPlayerIndex++;
            else
                currentPlayerIndex = 0;
        }
        while(!getCurrentPlayer().isActive());

        return players.get(currentPlayerIndex).getClientID();
    }


    public void nextState(){
        switch (currentState){
            case SETUP:
                currentState = GameState.NORMAL;
                turnHandler.startNextPlayerTimer();
                break;
            case NORMAL:
                currentState = GameState.FINAL_FRENZY;
                break;
            case FINAL_FRENZY:
                currentState = GameState.GAME_OVER;
                break;
        }
        this.currentGameBoard.setGameState(currentState);
    }

    public PlayerAbstract getPlayerFromId(int id){
        for(PlayerAbstract player : players){
            if(player.getClientID() == id){
                return player;
            }
        }
        return null;
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

    public List<PlayerAbstract> getPlayers() {
        return players;
    }

    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }

    public PlayerAbstract getPlayer(String string) {
        for(PlayerAbstract playerAbstract : players){
            if(playerAbstract.getName().equalsIgnoreCase(string))
                return playerAbstract;
        }
        return null;
    }

    public PlayerAbstract getPlayerFromColor(Color color){
        for(PlayerAbstract player : players){
            if(player.getColor() == color) return player;
        }
        return null;
    }

}