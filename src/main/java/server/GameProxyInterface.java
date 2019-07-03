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

     int getCurrentID(int clientID) throws RemoteException;

     /**
      * This method is used to send the client action to the controller
      */
     void makeAction(int clientID, Info action) throws RemoteException;

     void saveName(ReceiverInterface receiverInterface, Info info) throws RemoteException;

     void saveSetup(ReceiverInterface receiverInterface, Info info) throws RemoteException;

     void makeAsynchronousAction(int clientID, Info action)  throws RemoteException;

     void reconnect(int clientId) throws RemoteException;

     void ping() throws RemoteException;
}
