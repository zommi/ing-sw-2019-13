package client;

import server.GameProxyInterface;
import server.controller.playeraction.Action;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;
import view.ServerAnswer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class ConnectionSocket extends Connection {

    private int clientID;
    private String mapChoice;
    private GameModel gameModel;
    private int initialSkulls;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel();
    }

    public void addPlayerCharacter(String name){

    }

    public void saveAnswer(ServerAnswer answer){

    }

    public void sendGameModel(GameModel gameModel){

    }


    public boolean CharacterChoice(String name){
        return true;
    }


    public String getMap(){
        return this.mapChoice;
    }

    public int getClientID(){
        return this.clientID;
    }

    public void configure(){

    }

    public void send(ActionInfo action){

    }

    public void initializeSocket(){

    }

        public GameModel getGameModel(){
        return null;
    }

    public void add(String playerName, int map, int initialSkulls){

    }

    public int getInitialSkulls(){
        return this.initialSkulls;
    }

}
