package server;

import client.ReceiverInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public interface ServerInterface{

    public void run();

    public void addClient(ReceiverInterface client);
}
