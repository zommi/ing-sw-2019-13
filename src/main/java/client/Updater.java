package client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public abstract class Updater implements Observer {

    private GameModel gameModel;

    public abstract void update(Observable gameModel, Object object);

    public abstract void set() throws NotBoundException, RemoteException;

    public abstract void run();

    }
