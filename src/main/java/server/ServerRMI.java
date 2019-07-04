package server;

import constants.Constants;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerRMI implements Runnable {

    private Server server;
    private GameProxyInterface gameProxy = null;
    private final Registry registry;




    ServerRMI(Server server) throws RemoteException{

        registry = LocateRegistry.createRegistry(1099);
        this.server = server;
    }

    @Override
    public void run() {
        try {
            gameProxy = new GameProxy(this);
            registry.bind(Constants.REGISTRATION_ROOM_NAME, gameProxy);
            System.out.println("RMI started!!!");
        }
        catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Exception caught while binding...");
            e.printStackTrace();
        }
    }

    GameProxyInterface getGameProxy() {
        return gameProxy;
    }

    GameManager getGameManagerFromId(int clientID){
        return server.getGameManagerFromId(clientID);
    }

    public Server getServer() {
        return server;
    }
}
