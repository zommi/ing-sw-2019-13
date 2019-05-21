package server;

import client.Connection;
import client.Info;
import client.ReceiverInterface;
import server.model.player.PlayerAbstract;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameProxyInterface extends Remote {

    public int getStartGame() throws RemoteException;

    public String getMap() throws RemoteException;

    public boolean addPlayerCharacter(String name) throws RemoteException;

    public void startTimer() throws RemoteException;

    public boolean addMapPlayer() throws RemoteException;

    public boolean isCharacterTaken(String nameChar) throws RemoteException;

    public boolean sendInitialSkulls(int initialSkulls) throws RemoteException;

    public int getInitialSkulls() throws RemoteException;

    public boolean makeAction(int clientID, Info action) throws RemoteException;

    public int getClientID() throws RemoteException;

    public void register(ReceiverInterface client) throws RemoteException, NotBoundException;

    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException;

    public boolean sendPlayer(String name) throws RemoteException;

    public boolean sendMap(int numMap) throws RemoteException;

    public PlayerAbstract getPlayer() throws RemoteException;

    public void sendConnection(Connection connection) throws RemoteException;
}
