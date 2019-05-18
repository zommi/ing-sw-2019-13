package client;

import server.GameProxyInterface;
import server.GameProxyInterface;
import server.ServerAnswer.ServerAnswer;
import server.controller.playeraction.Action;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public abstract class Connection implements Serializable {

    private GameModel gamemodel;

    public abstract int getInitialSkulls();

    public abstract int getClientID();

    public abstract String getMap();

    public abstract void configure() throws RemoteException, NotBoundException;

    public abstract void send(Action action);

    public abstract GameProxyInterface initializeRMI() throws RemoteException, NotBoundException, AlreadyBoundException;

    public abstract GameModel getGameModel();

    public abstract void add(String playerName, int map, int initialSkulls) throws RemoteException;
}