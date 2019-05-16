package server.controller;

import exceptions.WrongGameStateException;
import server.controller.turns.TurnHandler;
import server.model.game.Game;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.util.List;
import java.util.Map;

public class Controller implements MyObserver {

    //TODO map che collega player a giocatore

    private Map<PlayerAbstract, Integer> playerToClient;

    private Game currentGame;

    private GameMap currentMap;

    private List<PlayerAbstract> currentPlayers;

    private PlayerAbstract activePlayer;

    private TurnHandler turnHandler;


    public Controller(int mapChoice, int initialSkulls){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.currentPlayers = this.currentGame.getActivePlayers();
        this.activePlayer = null;
    }

    public void nextPlayer() throws WrongGameStateException {
        this.currentGame.nextPlayer();
    }

    public void addClientInMap(PlayerAbstract player, int clientID){
        this.playerToClient.put(player, clientID);
    }


    public void update(){}

}
