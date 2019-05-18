package server;

import client.ReceiverInterface;
import server.controller.Controller;
import server.model.player.PlayerAbstract;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI implements Runnable, ServerInterface {

    private Server server;
    private Controller controller;
    private int clientIDadded;
    GameProxyInterface gameProxy = null;
    public static List<Integer> listOfClients = new ArrayList<Integer>();


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
        if(listOfClients.size() == 0){
            listOfClients = new ArrayList<>();
            listOfClients.add(0);
            this.clientIDadded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(clientIDadded +1);
            this.clientIDadded = clientIDadded + 1;
        }
        System.out.println("Added the clientID ");
        return clientIDadded;
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
