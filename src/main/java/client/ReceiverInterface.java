package client;

import view.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReceiverInterface extends Remote {
    public abstract void publishMessage(ServerAnswer answer) throws RemoteException;
    public abstract int getClientID() throws RemoteException;
}
