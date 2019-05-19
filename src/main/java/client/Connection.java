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

    public abstract void addPlayerCharacter(String name);

    public abstract boolean CharacterChoice(String name);

    public abstract int getInitialSkulls();

    public abstract int getClientID();

    public abstract void send(ActionInfo action);

    public abstract String getMap();

    public abstract void configure() throws RemoteException, NotBoundException;

    public abstract GameModel getGameModel();

    public abstract void add(String playerName, int map, int initialSkulls) throws RemoteException;
}