package client;

import exceptions.NotEnoughPlayersException;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public interface Updater extends Observer {


    public abstract void update(Observable gameModel, Object object);

    public abstract void set() throws NotBoundException, RemoteException, NotEnoughPlayersException;

    public abstract void run();

    }
