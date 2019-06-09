package server;

import client.Connection;
import client.Info;
import client.ReceiverInterface;
import exceptions.GameAlreadyStartedException;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameProxyInterface extends Remote {

    public int getStartGame() throws RemoteException;

    public String getCurrentCharacter() throws RemoteException;

    public void setClientHasChosenPowerup() throws RemoteException;

    public int getCurrentID() throws RemoteException;

    public int getGrenadeID() throws RemoteException;

    public String getMapName() throws RemoteException;

    public GameMap getMap() throws RemoteException;

    public boolean addPlayerCharacter(String name, int ID) throws RemoteException;

    public boolean addMapPlayer(int clientID) throws RemoteException;

    public boolean isCharacterTaken(String nameChar) throws RemoteException;

    public List<ReceiverInterface> getClientRMIadded() throws RemoteException;

    public boolean sendInitialSkulls(int initialSkulls) throws RemoteException;

    public int getInitialSkulls() throws RemoteException;

    public boolean makeAction(int clientID, Info action) throws RemoteException;

    public int getClientID() throws RemoteException;

    public void register(ReceiverInterface client) throws RemoteException, NotBoundException, GameAlreadyStartedException;

    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException;

    public boolean sendPlayer(String name, int ID) throws RemoteException;

    public boolean sendMap(int numMap) throws RemoteException;

    public PlayerAbstract getPlayer(int clientID) throws RemoteException;

}
