package server.controller;

import client.Info;
import exceptions.WrongGameStateException;
import server.Server;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
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

    private Game game;

    private Server server;


    public Controller(int mapChoice, int initialSkulls, Server server){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        //this.currentPlayers = this.currentGame.getActivePlayers();
        this.activePlayer = null;
        this.server = server;
    }

    public int getCurrentID(){
        return this.currentID;
    }

    public String getCurrentCharacter(){
        for(int i = 0; i < game.getActivePlayers().size(); i++){
            if(game.getActivePlayers().get(i).getClientID() == currentID)
                return game.getActivePlayers().get(i).getCharacterName();
        }
        return "No one is playing";
    }

    public boolean makeAction(int clientID, Info anction){
        TurnHandler turnHandler = game.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        //TODO initialize currentID and handle the turns.
        ConcretePlayer currentPlayer = (ConcretePlayer) game.getCurrentPlayer();


        if (currentPlayer.getPlayerState().equals(PlayerState.DISCONNECTED) || game.getCurrentState().equals(GameState.END_GAME)) {
            return false;
        }

        return true;
    }

    public Game getGame(){
        return this.game;
    }

    public void nextPlayer() throws WrongGameStateException {
        this.currentGame.nextPlayer();
    }

    public void addClientInMap(PlayerAbstract player){
        try{
            this.game.addPlayer(player);
        }
        catch(WrongGameStateException e){
            System.out.println("Wrong game state exception");
            e.printStackTrace();
        }
    }

    public List<PlayerAbstract> getPlayers(){
        return game.getActivePlayers();
    }

    public void update(){}

    public void startGame(){

    }

}
