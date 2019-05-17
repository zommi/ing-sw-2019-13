package client;

import server.GameProxyInterface;
import server.controller.playeraction.Action;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class ConnectionSocket extends Connection {

    private int clientID;
    private String mapChoice;
    private GameModel gameModel;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel();
    }

    public String getMap(){
        return this.mapChoice;
    }

    public int getClientID(){
        return this.clientID;
    }

    public void configure() throws RemoteException, NotBoundException{

    }

    public void send(Action action){

    }

    public GameProxyInterface initializeRMI() throws RemoteException, NotBoundException, AlreadyBoundException{
        return null;
    }

    public GameModel getGameModel(){
        return null;
    }

    public void add(String playerName, int map){

    }
}
