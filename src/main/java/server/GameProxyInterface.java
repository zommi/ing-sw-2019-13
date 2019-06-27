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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameProxyInterface extends Remote {

    public int getStartGame(int clientID) throws RemoteException;

    public String getCurrentCharacter(int clientID) throws RemoteException;

    public boolean askClient(int ID) throws RemoteException;

    public List<Info> getGrenadeAction(int grenadeID) throws RemoteException;

    public String getCharacterName(int clientID) throws RemoteException;

    //public void setClientHasChosenPowerup() throws RemoteException;

    public int getCurrentID(int clientID) throws RemoteException;

    public int getGrenadeID(int clientID) throws RemoteException;

    public String getMapName() throws RemoteException;

    public boolean addPlayerCharacter(String name, int ID) throws RemoteException;

    public boolean addMapPlayer(int clientID) throws RemoteException;

    public boolean isCharacterTaken(String nameChar, int clientID) throws RemoteException;

/*
    public Map<Integer, ReceiverInterface> getClientRMIadded() throws RemoteException;
*/

    public List<ReceiverInterface> getClientsRMIadded() throws RemoteException;

    public boolean sendInitialSkulls(int initialSkulls, int clientID) throws RemoteException;

    public int getInitialSkulls(int clientID) throws RemoteException;

    public boolean makeAction(int clientID, Info action) throws RemoteException;

    void saveName(ReceiverInterface receiverInterface, Info info) throws RemoteException;

    void saveSetup(ReceiverInterface receiverInterface, Info info) throws RemoteException;

    public boolean makeAsynchronousAction(int clientID, Info action)  throws RemoteException;

    public int getClientID() throws RemoteException;

    public int getClientID(ReceiverInterface receiverInterface) throws RemoteException;

    public void register(Info action, ReceiverInterface client) throws RemoteException, NotBoundException, GameAlreadyStartedException;

    public void setClientRMI(int id, ReceiverInterface clientRMI) throws RemoteException;

    public boolean sendPlayer(String name, int ID) throws RemoteException;

    public boolean sendMap(int numMap, int clientID) throws RemoteException;

    public PlayerAbstract getPlayer(int clientID) throws RemoteException;

    public List<PlayerAbstract> getPlayerList() throws RemoteException;

    void reconnect(int clientId) throws RemoteException;
}
