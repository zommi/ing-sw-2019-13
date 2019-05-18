package server;

import client.ReceiverInterface;
import server.controller.Controller;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;


public class ServerRMI implements Runnable, ServerInterface {

    private Server server;
    private Controller controller;
    private GameProxyInterface gameProxy = null;


    public Server getServer(){
        return this.server;
    }

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
    public int addClient(ReceiverInterface client){
        return this.server.addClient(client);
    }

    public void addMapClient() {
        try {
            System.out.println("Trying to add the client to the map ");
            controller.addClientInMap(gameProxy.getPlayer(), this.server.getClientIDadded());
        } catch (RemoteException re) {
            System.out.println("Could not add the client to the map ");
        }
    }
}