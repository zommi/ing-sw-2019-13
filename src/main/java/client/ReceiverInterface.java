package client;

import view.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReceiverInterface extends Remote {
    public void publishMessage(ServerAnswer answer) throws RemoteException;
    public int getClientID() throws RemoteException;
}
