package client;

import server.GameProxyInterface;
import server.controller.playeraction.Action;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.rmi.RemoteException;
import java.util.Map;

public class ConnectionSocket extends Connection {

    private int clientID;
    private GameModel gameModel;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel(clientID);
    }

    @Override
    public void configure() throws RemoteException{

    }

    @Override
    public void send(Action action){

    }

    @Override
    public GameModel getGameModel(){
        return null;
    }

    @Override
    public GameProxyInterface initializeRMI() throws RemoteException{
        return null;
    }

    @Override
    public void add(PlayerAbstract player, int map){

    }
}
