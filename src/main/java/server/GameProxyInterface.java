package server;

import client.ReceiverInterface;
import server.controller.playeraction.Action;
import server.model.player.PlayerAbstract;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameProxyInterface extends Remote {

    public boolean makeAction(int clientID, Action action) throws RemoteException;

    public void register(ReceiverInterface client) throws RemoteException, NotBoundException;

    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException;

    public boolean sendPlayer(String name) throws RemoteException;

    public boolean sendMap(int numMap) throws RemoteException;

    public PlayerAbstract getPlayer() throws RemoteException;

}
