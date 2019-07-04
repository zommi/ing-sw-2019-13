package client;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public interface Updater extends Observer {


    void update(Observable gameModel, Object object);

    void set() throws NotBoundException, RemoteException;

    void run();

}
