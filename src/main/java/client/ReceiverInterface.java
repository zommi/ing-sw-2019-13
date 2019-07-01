package client;

import view.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReceiverInterface extends Remote {

    void publishMessage(ServerAnswer answer) throws RemoteException;

    int getClientID() throws RemoteException;

    void setClientId(int clientId) throws RemoteException;

    void ping() throws RemoteException;
}
