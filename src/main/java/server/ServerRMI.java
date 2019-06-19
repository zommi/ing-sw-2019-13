package server;

import client.ReceiverInterface;
import constants.Constants;
import server.model.player.PlayerAbstract;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.List;


public class ServerRMI implements Runnable {

    private Server server;
    private GameProxyInterface gameProxy = null;
    private final Registry registry;






    public ServerRMI(Server server) throws RemoteException{

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

    public List<ReceiverInterface> getClientsAdded() {
        try {
            return gameProxy.getClientsRMIadded();
        }
        catch(RemoteException e){
            return Collections.emptyList();
        }
    }

    public int addClient(){
        return server.addClient();
    }

    public void addMapClient(PlayerAbstract p) {
        System.out.println("Trying to add the client to the map ");
        server.getGameManagerFromId(p.getClientID()).getController().addClientInMap(p);
    }

    public GameProxyInterface getGameProxy() {
        return gameProxy;
    }

    public GameManager getGameManagerFromId(int clientID){
        return server.getGameManagerFromId(clientID);
    }

    public Server getServer() {
        return server;
    }
}
