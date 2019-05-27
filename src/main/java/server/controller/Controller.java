package server.controller;

import client.CollectInfo;
import client.Info;
import client.MoveInfo;
import client.weapons.ShoootInfo;
import client.weapons.ShootParser;
import client.weapons.Weapon;
import exceptions.WrongGameStateException;
import server.Server;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.gameboard.GameBoard;
import server.model.map.GameMap;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller implements MyObserver {

    //private List<PlayerAbstract> players = new ArrayList<>();

    private int currentID;

    private Game currentGame;

    private GameMap currentMap;

    private List<PlayerAbstract> currentPlayers;

    private PlayerAbstract activePlayer;

    private TurnHandler turnHandler;

    private Server server;


    public Controller(int mapChoice, int initialSkulls, Server server){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.activePlayer = null;
        this.server = server;
    }

    public int getCurrentID(){
        return this.currentID;
    }

    public String getCurrentCharacter(){
        for(int i = 0; i < currentGame.getActivePlayers().size(); i++){
            if(currentGame.getActivePlayers().get(i).getClientID() == currentID)
                return currentGame.getActivePlayers().get(i).getCharacterName();
        }
        return "No one is playing";
    }

    public boolean makeAction(int clientID, Info action){
        TurnHandler turnHandler = currentGame.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        //TODO initialize currentID and handle the turns.
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();


        if (currentPlayer.getPlayerState().equals(PlayerState.DISCONNECTED) || currentGame.getCurrentState().equals(GameState.END_GAME)) {
            return false;
        }


        if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action);
            turnHandler.setAndDoAction((Action)moveAction);
        }
        else if(action instanceof CollectInfo){
            MoveInfo temp0 = new MoveInfo(((CollectInfo)action).getCoordinateX(),((CollectInfo)action).getCoordinateY());
            CollectAction collectAction = new CollectAction(temp0, (CollectInfo) action);
            turnHandler.setAndDoAction(collectAction);
        }
        else if(action instanceof ShoootInfo){
            ShootAction shootAction = new ShootAction((ShoootInfo) action);
            turnHandler.setAndDoAction(shootAction);
        }



        return true;
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }

    public void nextPlayer() throws WrongGameStateException {
        this.currentGame.nextPlayer();
    }

    public void addClientInMap(PlayerAbstract player){
        try{
            this.currentGame.addPlayer(player);
        }
        catch(WrongGameStateException e){
            System.out.println("Wrong game state exception");
            e.printStackTrace();
        }
    }

    public List<PlayerAbstract> getPlayers(){
        return currentGame.getActivePlayers();
    }

    public void update(){}

    public void startGame(){

    }

}
