package server;

import client.Connection;
import client.ReceiverInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public interface ServerInterface{

    public void run();

    public int addClient(ReceiverInterface client);
}
