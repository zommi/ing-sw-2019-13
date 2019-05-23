package server.controller;

import exceptions.WrongGameStateException;
import server.controller.turns.TurnHandler;
import server.model.game.Game;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

public class Controller implements MyObserver {

    private List<PlayerAbstract> players = new ArrayList<>();

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

    public void addClientInMap(PlayerAbstract player){
        this.players.add(player);
    }

    public List<PlayerAbstract> getPlayers(){
        return this.players;
    }

    public void update(){}

}
