package client;

import view.ServerAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReceiverInterface extends Remote  {
    public void publishMessage(ServerAnswer answer) throws RemoteException;

    public int getClientID() throws RemoteException;

    public boolean askClient() throws RemoteException;

    public List<Info> getGrenadeAction() throws RemoteException;

    public int  getNumberOfGrenades() throws RemoteException;

    public void setClientIDExisting(int idAlreadyExisting) throws RemoteException;
}
