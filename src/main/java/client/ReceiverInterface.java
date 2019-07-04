package client;

import answers.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReceiverInterface extends Remote {

    /**
     * This method is called from the server to publish a message on a client
     * @throws RemoteException
     */
    void publishMessage(ServerAnswer answer) throws RemoteException;

    /**
     * This method asks to the server the clientID of the client
     * @throws RemoteException
     */
    int getClientID() throws RemoteException;

    /**
     * This method is called from the server to set the clientID on a client
     * @throws RemoteException
     */
    void setClientId(int clientId) throws RemoteException;

    /**
     * This method is called from the server to ping a client
     * @throws RemoteException
     */
    void ping() throws RemoteException;
}
