package server;

import client.info.Info;
import client.ReceiverInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
