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

     int getStartGame(int clientID) throws RemoteException;

     String getCurrentCharacter(int clientID) throws RemoteException;

     boolean askClient(int ID) throws RemoteException;

     List<Info> getGrenadeAction(int grenadeID) throws RemoteException;

     String getCharacterName(int clientID) throws RemoteException;

     int getCurrentID(int clientID) throws RemoteException;

     int getGrenadeID(int clientID) throws RemoteException;

     boolean makeAction(int clientID, Info action) throws RemoteException;

     void saveName(ReceiverInterface receiverInterface, Info info) throws RemoteException;

     void saveSetup(ReceiverInterface receiverInterface, Info info) throws RemoteException;

     boolean makeAsynchronousAction(int clientID, Info action)  throws RemoteException;

     PlayerAbstract getPlayer(int clientID) throws RemoteException;

     void reconnect(int clientId) throws RemoteException;

     void ping() throws RemoteException;
}
