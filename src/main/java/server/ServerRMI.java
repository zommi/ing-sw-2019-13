package server;

import client.ReceiverInterface;
import server.controller.Controller;
import server.model.player.PlayerAbstract;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI implements Runnable, ServerInterface {

    private Server server;
    private Controller controller;
    private int clientIDadded;
    GameProxyInterface gameProxy = null;


    public ServerRMI(Server server) {
        this.server = server;
    }


    @Override
    public void run() {
        try {
            gameProxy = new GameProxy(this);
            server.getRegistry().bind(server.getName(), gameProxy);
            System.out.println("RMI started!!!");
        }
        catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Exception caught while binding...");
            e.printStackTrace();
        }
    }

    protected void setController(Controller controller){
        this.controller = controller;
    }

    @Override
    public void addClient(ReceiverInterface client){
        try{
            this.clientIDadded = client.getClientID();
            System.out.println("Added the clientID ");
        }
        catch(RemoteException re){
            System.out.println("Could not add the clientID ");
        }
    }

    public void addMapClient() {
        try {
            System.out.println("Trying to add the client to the map ");
            controller.addClientInMap(gameProxy.getPlayer(), clientIDadded);
        } catch (RemoteException re) {
            System.out.println("Could not add the client to the map ");
        }
    }
}
