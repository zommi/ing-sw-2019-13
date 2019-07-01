package server.model.game;

import exceptions.*;
import constants.Color;
import server.controller.Controller;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.cards.AmmoTile;
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

    private Controller controller;

    private TurnHandler turnHandler;

    private GameState currentState;

    private GameMap currentGameMap;

    private List<PlayerAbstract> activePlayers;

    private GameBoard currentGameBoard;

    private int currentPlayerIndex;

    private int lastPlayerIndex;

    private WeaponDeck weaponDeck;

    private ListOfWeaponsAnswer weaponList = new ListOfWeaponsAnswer();

    public Game(int mapChoice, int skullChoice, Controller controller) {
        this.currentState = GameState.SETUP;
        this.currentGameBoard = new GameBoard(mapChoice,skullChoice);
        this.currentGameMap = currentGameBoard.getMap();
        this.activePlayers = new ArrayList<>();
        this.weaponDeck = currentGameBoard.getWeaponDeck();
        this.currentPlayerIndex = 0;
        this.lastPlayerIndex = 0;
        this.controller = controller;
        this.turnHandler = new TurnHandler(controller);
        this.currentGameBoard.setGameState(currentState);
        for(int i = 0; i < weaponDeck.getSize(); i++){
            WeaponCard temp = weaponDeck.getWeaponFromIndex(i);
            this.weaponList.add(temp);
            //System.out.println("added the weapon " +weaponDeck.getWeaponFromIndex(i).getName());
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < currentGameMap.getSpawnPoints().size(); j++){
                WeaponCard temp1 = currentGameMap.getSpawnPoints().get(j).getWeaponCards().get(i);
                this.weaponList.add(temp1);
            }
        }
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

    public void addPlayer(PlayerAbstract player) {
        if (this.currentState == GameState.SETUP) {
            this.activePlayers.add(player);
            this.currentGameBoard.addPlayerBoard((ConcretePlayer) player);
        }
    }

    public void removePlayer(PlayerAbstract player) throws WrongGameStateException{
        if(this.currentState == GameState.SETUP){
            this.activePlayers.remove(player);
        }else throw new WrongGameStateException();
    }

    public PlayerAbstract getCurrentPlayer(){
        return activePlayers.get(currentPlayerIndex);
    }

    public PlayerAbstract getLastPlayer(){return activePlayers.get(lastPlayerIndex);}

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int nextPlayer(){
        lastPlayerIndex = currentPlayerIndex;
        do{
            if (currentPlayerIndex < this.activePlayers.size() - 1)
                currentPlayerIndex++;
            else
                currentPlayerIndex = 0;
        }
        while(!getCurrentPlayer().isConnected());

        return activePlayers.get(currentPlayerIndex).getClientID();
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
        for(PlayerAbstract player : activePlayers){
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

    public List<PlayerAbstract> getActivePlayers() {
        return activePlayers;
    }

    public void setPlayersNames(){
        List<String> names = new ArrayList<>();
        for(PlayerAbstract playerAbstract : activePlayers){
            names.add(playerAbstract.getName());
        }
        this.currentGameBoard.setPlayerNames(names);
    }

    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }

    public PlayerAbstract getPlayer(String string) {
        for(PlayerAbstract playerAbstract : activePlayers){
            if(playerAbstract.getName().equalsIgnoreCase(string))
                return playerAbstract;
        }
        return null;
    }

    public PlayerAbstract getPlayerFromColor(Color color){
        for(PlayerAbstract player : activePlayers){
            if(player.getColor() == color) return player;
        }
        return null;
    }

    public boolean isAfterFirstPlayer(PlayerAbstract playerAbstract) {
        List<PlayerAbstract> playersAfter = activePlayers.subList(0,currentPlayerIndex);
        return playersAfter.contains(playerAbstract);
    }
}