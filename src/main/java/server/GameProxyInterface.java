package server;

import client.Connection;
import client.Info;
import client.ReceiverInterface;
import exceptions.GameAlreadyStartedException;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameProxyInterface extends Remote {

    public int getStartGame() throws RemoteException;

    public String getCurrentCharacter() throws RemoteException;

    public int getCurrentID() throws RemoteException;

    public String getMap() throws RemoteException;

    public boolean addPlayerCharacter(String name, int ID) throws RemoteException;

    /*public void startMatch() throws RemoteException;*/

    public boolean addMapPlayer() throws RemoteException;

    public boolean isCharacterTaken(String nameChar) throws RemoteException;

    public List<ReceiverInterface> getClientRMIadded() throws RemoteException;

    public boolean sendInitialSkulls(int initialSkulls) throws RemoteException;

    public int getInitialSkulls() throws RemoteException;

    public boolean makeAction(int clientID, Info action) throws RemoteException;

    public int getClientID() throws RemoteException;

    public void register(Connection client) throws RemoteException, NotBoundException, GameAlreadyStartedException;

    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException;

    public boolean sendPlayer(String name, int ID) throws RemoteException;

    public boolean sendMap(int numMap) throws RemoteException;

    public PlayerAbstract getPlayer() throws RemoteException;
}
