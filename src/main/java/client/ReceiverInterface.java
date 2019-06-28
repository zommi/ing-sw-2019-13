package client;

import view.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReceiverInterface extends Remote {
    void publishMessage(ServerAnswer answer) throws RemoteException;

    int getClientID() throws RemoteException;

    boolean askClient() throws RemoteException;

    List<Info> getGrenadeAction() throws RemoteException;

    int  getNumberOfGrenades() throws RemoteException;

    void setClientIDExisting(int idAlreadyExisting) throws RemoteException;

    void setClientId(int clientId) throws RemoteException;

    void ping() throws RemoteException;
}
