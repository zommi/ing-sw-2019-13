package server;

import client.Connection;
import client.ReceiverInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Runnable{

    void run();

    List<ReceiverInterface> getClientsAdded();

    //public int addClient(ReceiverInterface client);
}
