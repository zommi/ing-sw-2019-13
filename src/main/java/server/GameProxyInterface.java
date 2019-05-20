package server;

import client.GameModel;
import client.Info;
import client.ReceiverInterface;
import server.controller.playeraction.Action;
import server.model.player.PlayerAbstract;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameProxyInterface extends Remote {


    public String getMap() throws RemoteException;

    public boolean addPlayerCharacter(String name) throws RemoteException;

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

    void sendGameModel(GameModel gameModel) throws RemoteException;
}
